package dao;

import library.Member;
import util.DBUtil;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MemberDAO {

    public static List<Member> getAllMembers() {
        List<Member> members = new ArrayList<>();
        String sql = "SELECT MemberID, Name, Username, Email FROM Members";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                members.add(new Member(rs.getInt("MemberID"), rs.getString("Name"), 
                                     rs.getString("Username"), rs.getString("Email")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return members;
    }

    public static boolean addMember(String name, String username, String email) {
        String sql = "INSERT INTO Members (Name, Username, Email) VALUES (?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, username);
            ps.setString(3, email);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // ✅ ميثود التعديل اللي كانت ناقصة ومسببة إيرور
    public static boolean updateMember(Member member) {
        String sql = "UPDATE Members SET Name = ?, Email = ? WHERE MemberID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, member.getName());
            ps.setString(2, member.getEmail());
            ps.setInt(3, member.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public static boolean deleteMember(int id) {
        String sql = "DELETE FROM Members WHERE MemberID = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}