package dao;

import library.*;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReservationDAO {

    public static List<Reservation> getAllReservations() {
        List<Reservation> list = new ArrayList<>();
        String sql = "SELECT r.ReservationID, r.BookID, r.MemberID, r.ReservationDate, " +
                     "m.Name as MemberName, b.Title as BookTitle FROM Reservations r " +
                     "JOIN Members m ON r.MemberID = m.MemberID " +
                     "JOIN Books b ON r.BookID = b.BookID " +
                     "WHERE r.Status = 'PENDING'";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Member m = new Member(rs.getInt("MemberID"), rs.getString("MemberName"), "", "");
                Book b = new Book(rs.getInt("BookID"), rs.getString("BookTitle"), "", "", null, 0, 0, 0, 0);
                list.add(new Reservation(rs.getInt("ReservationID"), m, b, rs.getDate("ReservationDate").toLocalDate()));
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    // ✅ ميثود تحويل الحجز لإعارة (كاملة ومظبوطة)
    public static boolean confirmAndIssue(int resId, int bookId, int memberId) {
        String sqlLoan = "INSERT INTO Loans (BookID, MemberID, LoanDate, DueDate) VALUES (?, ?, GETDATE(), DATEADD(day, 14, GETDATE()))";
        String sqlUpdateRes = "UPDATE Reservations SET Status = 'COMPLETED' WHERE ReservationID = ?";
        // هنا مش هننقص الكتاب لأنه أوردي نقص وقت الحجز (لو مشيت ورا التعديل اللي عملته تحت)
        
        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlLoan);
                 PreparedStatement ps2 = conn.prepareStatement(sqlUpdateRes)) {
                
                ps1.setInt(1, bookId); ps1.setInt(2, memberId); ps1.executeUpdate();
                ps2.setInt(1, resId); ps2.executeUpdate();
                
                conn.commit();
                return true;
            } catch (SQLException e) { conn.rollback(); }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    // 🌟 تعديل ميثود الإضافة عشان تتأكد إن فيه نسخ متاحة وتعمل نقص للمتاح
    public static boolean addReservation(int bookId, int memberId) {
        String checkSql = "SELECT CopiesAvailable FROM Books WHERE BookID = ?";
        String insertSql = "INSERT INTO Reservations (BookID, MemberID, ReservationDate, Status) VALUES (?, ?, GETDATE(), 'PENDING')";
        String updateBookSql = "UPDATE Books SET CopiesAvailable = CopiesAvailable - 1 WHERE BookID = ? AND CopiesAvailable > 0";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false); // بداية الـ Transaction
            
            try (PreparedStatement psCheck = conn.prepareStatement(checkSql);
                 PreparedStatement psInsert = conn.prepareStatement(insertSql);
                 PreparedStatement psUpdate = conn.prepareStatement(updateBookSql)) {
                
                // 1. التأكد من الإتاحة
                psCheck.setInt(1, bookId);
                ResultSet rs = psCheck.executeQuery();
                if (rs.next() && rs.getInt("CopiesAvailable") > 0) {
                    
                    // 2. إضافة الحجز
                    psInsert.setInt(1, bookId);
                    psInsert.setInt(2, memberId);
                    psInsert.executeUpdate();
                    
                    // 3. تحديث عدد النسخ
                    psUpdate.setInt(1, bookId);
                    if (psUpdate.executeUpdate() > 0) {
                        conn.commit(); // تنفيذ الكل لو تمام
                        return true;
                    }
                }
                conn.rollback(); // تراجع لو مفيش نسخ
            } catch (SQLException e) { conn.rollback(); }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }

    public static boolean deleteReservation(int id) {
        // ⚠️ ملاحظة: لو مسحت حجز لازم ترجع النسخة للمتاح في جدول الكتب
        String sqlGetBook = "SELECT BookID FROM Reservations WHERE ReservationID = ?";
        String sqlDelete = "DELETE FROM Reservations WHERE ReservationID = ?";
        String sqlReturnBook = "UPDATE Books SET CopiesAvailable = CopiesAvailable + 1 WHERE BookID = ?";

        try (Connection conn = DBUtil.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement ps1 = conn.prepareStatement(sqlGetBook);
                 PreparedStatement ps2 = conn.prepareStatement(sqlDelete);
                 PreparedStatement ps3 = conn.prepareStatement(sqlReturnBook)) {
                
                ps1.setInt(1, id);
                ResultSet rs = ps1.executeQuery();
                if (rs.next()) {
                    int bookId = rs.getInt("BookID");
                    ps2.setInt(1, id); ps2.executeUpdate();
                    ps3.setInt(1, bookId); ps3.executeUpdate();
                    conn.commit();
                    return true;
                }
            } catch (SQLException e) { conn.rollback(); }
        } catch (SQLException e) { e.printStackTrace(); }
        return false;
    }
}