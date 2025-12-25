package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import javax.swing.ImageIcon;
import java.net.URL;

public class DBUtil {

    // ✅ بيانات الاتصال بـ SQL Server
    private static final String SERVER_NAME = "AHMED\\JAVASQL";
    private static final String DB_USER = "sa";
    private static final String DB_PASSWORD = "123456";
    private static final String DB_URL = 
            "jdbc:sqlserver://" + SERVER_NAME + ";databaseName=LibrarySystemDB;" +
            "encrypt=true;trustServerCertificate=true;";
    
    private static final String JDBC_DRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

    // 1️⃣ ميثود الاتصال بقاعدة البيانات
    public static Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName(JDBC_DRIVER);
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            return connection;
        } catch (Exception e) {
            System.err.println("Database Connection Failed!");
            System.err.println("Error details: " + e.getMessage());
            return null;
        }
    }

    // 2️⃣ ميثود وضع الأيقونة (تعديل: تستقبل Window لتشمل JFrame و JDialog)
    public static void setWindowIcon(java.awt.Window window) {
        try {
            URL imgURL = DBUtil.class.getResource("/icon.png");
            if (imgURL != null) {
                window.setIconImage(new ImageIcon(imgURL).getImage());
            } else {
                System.err.println("Icon not found in resources!");
            }
        } catch (Exception e) {
            // صامت عشان ميعطلش البرنامج لو الصورة فيها مشكلة
        }
    }

    // 3️⃣ دوال إغلاق الموارد
    public static void close(Connection connection) {
        if (connection != null) {
            try { connection.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void close(Statement statement) {
        if (statement != null) {
            try { statement.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }

    public static void close(ResultSet resultSet) {
        if (resultSet != null) {
            try { resultSet.close(); } catch (SQLException e) { e.printStackTrace(); }
        }
    }
}