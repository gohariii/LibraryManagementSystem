package dao;

import library.Category;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryDAO {

    // 1. جلب كل التصنيفات مع الوصف
    public static List<Category> getAllCategories() {
        List<Category> list = new ArrayList<>();
        // تحديث الاستعلام ليجلب الاسم والوصف
        String sql = "SELECT CategoryID, Name, Description FROM Categories"; 
        try (Connection conn = DBUtil.getConnection(); 
             Statement st = conn.createStatement(); 
             ResultSet rs = st.executeQuery(sql)) {
            
            while (rs.next()) {
                // نأخذ الوصف الحقيقي من الداتا بيز
                list.add(new Category(
                    rs.getInt("CategoryID"), 
                    rs.getString("Name"), 
                    rs.getString("Description")
                ));
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
        return list;
    }

    // 2. إضافة تصنيف جديد مع الوصف
    public static boolean addCategory(String name, String description) {
        String sql = "INSERT INTO Categories (Name, Description) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, description); // سيتم تخزينه حتى لو كان فارغاً
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            return false; 
        }
    }

    // 3. حذف تصنيف
    public static boolean deleteCategory(int id) {
        String sql = "DELETE FROM Categories WHERE CategoryID = ?";
        try (Connection conn = DBUtil.getConnection(); 
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { 
            return false; 
        }
    }
}