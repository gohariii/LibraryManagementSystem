package library;

import javax.swing.*;
import util.DBUtil; 
import java.sql.Connection; 
import java.net.URL;

/**
 * الكلاس الرئيسي لتشغيل نظام إدارة المكتبة (FCI Library System).
 * تم تصميمه ليتوافق مع هيكلية مشروع Maven.
 */
public class Library {

    public static void main(String[] args) {
        
        // 1. تحسين شكل الواجهة (Nimbus Look and Feel)
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            // استخدام التصميم الافتراضي للنظام في حالة وجود خطأ
        }

        // 2. التحقق من الاتصال بقاعدة البيانات قبل فتح البرنامج
        // لو الداتابيز مش شغالة، البرنامج مش هيفتح وهيطلع رسالة تنبيه
        Connection testConnection = DBUtil.getConnection();
        if (testConnection != null) {
            System.out.println("Database Connected Successfully!");
            DBUtil.close(testConnection); 
        } else {
            JOptionPane.showMessageDialog(null, 
                "خطأ في الاتصال بقاعدة البيانات!\nتأكد من تشغيل XAMPP (Apache & MySQL).", 
                "Database Connection Error", 
                JOptionPane.ERROR_MESSAGE);
            return; // إنهاء التشغيل فوراً
        }

        // 3. تشغيل الواجهة الرسومية في الـ Thread المخصص لها
        SwingUtilities.invokeLater(() -> {
            try {
                // تعريف الكلاسات الأساسية (التي ظهرت في صورتك)
                LibrarySystem system = new LibrarySystem();
                LoginFrame login = new LoginFrame(system);
                
                // 4. محاولة تحميل الأيقونة (icon.png)
                // السطر ده بيبحث عنها في فولدر src/main/resources
                URL imgURL = Library.class.getResource("/icon.png");
                
                if (imgURL != null) {
                    ImageIcon icon = new ImageIcon(imgURL);
                    login.setIconImage(icon.getImage());
                } else {
                    // محاولة بديلة لو الطريقة الأولى فشلت (عشان المافن ساعات بيغلس)
                    imgURL = Thread.currentThread().getContextClassLoader().getResource("icon.png");
                    if (imgURL != null) {
                        login.setIconImage(new ImageIcon(imgURL).getImage());
                    } else {
                        System.err.println("Warning: icon.png not found. Please check src/main/resources folder.");
                    }
                }

                // 5. إعدادات ظهور شاشة تسجيل الدخول
                login.setTitle("Library Management System - Login");
                login.setLocationRelativeTo(null); // يفتح في وسط الشاشة
                login.setVisible(true);
                
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "حدث خطأ غير متوقع أثناء تشغيل البرنامج: " + e.getMessage());
                e.printStackTrace();
            }
        });
    }
}