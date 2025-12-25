package dao;

import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class LoanDAO {

    // 1. ميثود جديدة: جلب كل الإعارات النشطة بالأسماء والـ IDs
    public static List<Object[]> getAllActiveLoans() {
        List<Object[]> list = new ArrayList<>();
        // استعلام يربط الجداول ببعضها عشان نجيب الأسماء والـ IDs في نفس الوقت
        String sql = "SELECT L.LoanID, M.MemberID, M.Name, B.BookID, B.Title, L.LoanDate, L.DueDate " +
                     "FROM Loans L " +
                     "JOIN Members M ON L.MemberID = M.MemberID " +
                     "JOIN Books B ON L.BookID = B.BookID " +
                     "WHERE L.ActualReturnDate IS NULL"; 

        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                list.add(new Object[]{
                    rs.getInt("LoanID"),
                    rs.getInt("MemberID"),
                    rs.getString("Name"),
                    rs.getInt("BookID"),
                    rs.getString("Title"),
                    rs.getDate("LoanDate"),
                    rs.getDate("DueDate")
                });
            }
        } catch (SQLException e) {
            System.out.println("JOIN Error: " + e.getMessage());
        }
        return list;
    }

    // 2. ميثود تسجيل إعارة جديدة (اللي إنت باعتها)
    public static boolean issueLoan(int bookId, int memberId) {
        String sqlLoan = "INSERT INTO Loans (BookID, MemberID, LoanDate, DueDate) VALUES (?, ?, GETDATE(), DATEADD(day, 14, GETDATE()))";
        String sqlUpdateBook = "UPDATE Books SET CopiesAvailable = CopiesAvailable - 1 WHERE BookID = ? AND CopiesAvailable > 0";
        
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false); 

            try (PreparedStatement ps1 = conn.prepareStatement(sqlLoan)) {
                ps1.setInt(1, bookId);
                ps1.setInt(2, memberId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateBook)) {
                ps2.setInt(1, bookId);
                int rowsAffected = ps2.executeUpdate();
                if (rowsAffected == 0) throw new SQLException("No copies available!");
            }

            conn.commit(); 
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {} }
            System.out.println("DEBUG ERROR: " + e.getMessage()); 
            return false;
        } finally {
            DBUtil.close(conn);
        }
    }

    // 3. ميثود إرجاع كتاب (اللي إنت باعتها)
    public static boolean returnLoan(int loanId, int bookId) {
        String sqlReturn = "UPDATE Loans SET ActualReturnDate = GETDATE() WHERE LoanID = ?";
        String sqlUpdateBook = "UPDATE Books SET CopiesAvailable = CopiesAvailable + 1 WHERE BookID = ?";
        
        Connection conn = null;
        try {
            conn = DBUtil.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement ps1 = conn.prepareStatement(sqlReturn)) {
                ps1.setInt(1, loanId);
                ps1.executeUpdate();
            }

            try (PreparedStatement ps2 = conn.prepareStatement(sqlUpdateBook)) {
                ps2.setInt(1, bookId);
                ps2.executeUpdate();
            }

            conn.commit();
            return true;
        } catch (SQLException e) {
            if (conn != null) { try { conn.rollback(); } catch (SQLException ex) {} }
            return false;
        } finally {
            DBUtil.close(conn);
        }
    }
}