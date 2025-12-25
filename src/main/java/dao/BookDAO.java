package dao;

import library.Book;
import library.Category;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BookDAO {

    public static List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        // Query احترافية بـ LEFT JOIN و Subquery للحجوزات
        String sql = "SELECT b.BookID, b.Title, b.Author, b.ISBN, b.PublishedYear, b.TotalCopies, b.CopiesAvailable, " +
                     "c.CategoryID, c.Name as CategoryName, " +
                     "(SELECT COUNT(*) FROM Reservations r WHERE r.BookID = b.BookID AND r.Status = 'PENDING') as ReservedCount " +
                     "FROM Books b " +
                     "LEFT JOIN Categories c ON b.CategoryID = c.CategoryID";

        try (Connection conn = DBUtil.getConnection();
             Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                Category cat = null;
                int catId = rs.getInt("CategoryID");
                if (!rs.wasNull()) {
                    cat = new Category(catId, rs.getString("CategoryName"));
                } else {
                    cat = new Category(0, "No Category");
                }

                books.add(new Book(
                    rs.getInt("BookID"), 
                    rs.getString("Title"), 
                    rs.getString("Author"),
                    rs.getString("ISBN"), 
                    cat, 
                    rs.getInt("PublishedYear"),
                    rs.getInt("TotalCopies"), 
                    rs.getInt("CopiesAvailable"),
                    rs.getInt("ReservedCount")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return books;
    }

    public static boolean addBook(Book book) {
        String sql = "INSERT INTO Books (Title, Author, ISBN, CategoryID, PublishedYear, TotalCopies, CopiesAvailable) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            
            // معالجة الـ CategoryID لو 0 نبعته NULL للداتابيز
            if (book.getCategory() == null || book.getCategory().getId() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, book.getCategory().getId());
            }
            
            ps.setInt(5, book.getPublishedYear());
            ps.setInt(6, book.getTotalCopies());
            ps.setInt(7, book.getTotalCopies()); // المتاح في الأول هو الإجمالي
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    // ✅ الميثود دي كانت ناقصة ومهمة جداً للتعديل
    public static boolean updateBook(Book book) {
        String sql = "UPDATE Books SET Title=?, Author=?, ISBN=?, CategoryID=?, PublishedYear=?, TotalCopies=? WHERE BookID=?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, book.getTitle());
            ps.setString(2, book.getAuthor());
            ps.setString(3, book.getIsbn());
            
            if (book.getCategory() == null || book.getCategory().getId() == 0) {
                ps.setNull(4, Types.INTEGER);
            } else {
                ps.setInt(4, book.getCategory().getId());
            }
            
            ps.setInt(5, book.getPublishedYear());
            ps.setInt(6, book.getTotalCopies());
            ps.setInt(7, book.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            e.printStackTrace();
            return false; 
        }
    }

    public static boolean deleteBook(int id) {
        String sql = "DELETE FROM Books WHERE BookID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { return false; }
    }
}