package dao; 

import library.Admin;
import util.DBUtil; 
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminDAO {

    /**
     * يتحقق من وجود المدير ويسترجع بياناته من قاعدة البيانات
     * @param username اسم المستخدم
     * @param password كلمة المرور كنص عادي (نتوقع "123")
     * @return كائن Admin إذا كان تسجيل الدخول ناجحًا، أو null إذا فشل
     */
    public static Admin loginAdmin(String username, String password) {
        
        // الاستعلام للبحث عن المستخدم وكلمة المرور في جدول Admins
        String sql = "SELECT AdminID, Name, Email, Role FROM Admins WHERE Username = ? AND PasswordHash = ?";
        
        Connection connection = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        Admin admin = null;

        try {
            connection = DBUtil.getConnection(); 
            ps = connection.prepareStatement(sql);
            
            ps.setString(1, username);
            ps.setString(2, password); 
            
            rs = ps.executeQuery();
            
            if (rs.next()) {
                int id = rs.getInt("AdminID");
                String name = rs.getString("Name");
                String email = rs.getString("Email");
                String role = rs.getString("Role");
                
                // إنشاء كائن Admin 
                admin = new Admin(id, name, email, password, role);
            }
        } catch (SQLException e) {
            System.err.println("Database error during Admin login check: " + e.getMessage());
        } finally {
            DBUtil.close(rs);
            DBUtil.close(ps);
            DBUtil.close(connection);
        }
        return admin;
    }
}