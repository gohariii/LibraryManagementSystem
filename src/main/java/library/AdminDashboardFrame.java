package library;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import util.DBUtil; // استيراد اليوتل عشان الأيقونة والداتابيز

public class AdminDashboardFrame extends JFrame {
    
    private LibrarySystem system;
    private Admin loggedInAdmin; 
    private JPanel contentPanel; 

    public AdminDashboardFrame(LibrarySystem system, Admin admin) {
        this.system = system;
        this.loggedInAdmin = admin;
        
        // --- إعدادات النافذة الأساسية ---
        setTitle("Admin Dashboard - Welcome, " + admin.getName());
        setSize(1000, 750); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); 
        
        // ✅ السطر اللي كان ناقص عشان الأيقونة تثبت وما تختفيش
        util.DBUtil.setWindowIcon(this);
        
        setLayout(new BorderLayout());
        
        // --- 1. الجزء العلوي (Header) ---
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(45, 52, 71)); 
        JLabel headerLabel = new JLabel("Library Management System - Administrator Panel", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        headerLabel.setForeground(Color.WHITE); 
        headerLabel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));
        headerPanel.add(headerLabel);
        add(headerPanel, BorderLayout.NORTH);
        
        // --- 2. القائمة الجانبية (Sidebar) ---
        JPanel menuPanel = createMenuPanel();
        add(menuPanel, BorderLayout.WEST); 
        
        // --- 3. اللوحة المركزية (Content Area) ---
        contentPanel = new JPanel();
        contentPanel.setLayout(new BorderLayout());
        contentPanel.setBorder(BorderFactory.createLoweredBevelBorder());
        
        // رسالة الترحيب الافتراضية
        JLabel welcomeLabel = new JLabel("<html><div style='text-align: center;'>"
                + "<h1>Welcome back, " + admin.getName() + "</h1>"
                + "<p>Please select an option from the left menu to manage library operations.</p>"
                + "</div></html>", SwingConstants.CENTER);
        contentPanel.add(welcomeLabel, BorderLayout.CENTER);
        
        add(contentPanel, BorderLayout.CENTER);
        
        setVisible(true);
    }

    private JPanel createMenuPanel() {
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1, 10, 10)); 
        panel.setBorder(BorderFactory.createTitledBorder("Navigation"));
        panel.setPreferredSize(new Dimension(220, 0)); 

        // تعريف الأزرار
        JButton booksBtn = createStyledButton("Manage Books");
        JButton membersBtn = createStyledButton("Manage Members");
        JButton loansBtn = createStyledButton("Manage Loans");
        JButton categoriesBtn = createStyledButton("Manage Categories");
        JButton resBtn = createStyledButton("Manage Reservations");
        JButton logoutBtn = new JButton("Logout");

        // ربط الأزرار بالـ Panels (تأكد أن هذه الكلاسات موجودة في مشروعك)
        booksBtn.addActionListener(e -> showPanel(new BookManagementPanel())); 
        membersBtn.addActionListener(e -> showPanel(new MemberManagementPanel())); 
        loansBtn.addActionListener(e -> showPanel(new LoanManagementPanel()));
        categoriesBtn.addActionListener(e -> showPanel(new CategoryManagementPanel())); 
        resBtn.addActionListener(e -> showPanel(new ReservationManagementPanel())); 
        
        logoutBtn.addActionListener(this::logoutAction);
        logoutBtn.setBackground(new Color(192, 57, 43)); 
        logoutBtn.setForeground(Color.WHITE);

        // إضافة الأزرار للوحة
        panel.add(booksBtn);
        panel.add(membersBtn);
        panel.add(loansBtn);
        panel.add(categoriesBtn);
        panel.add(resBtn);
        panel.add(new JLabel("")); // مسافة فارغة
        panel.add(logoutBtn);

        return panel;
    }

    private JButton createStyledButton(String text) {
        JButton btn = new JButton(text);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 13));
        return btn;
    }

    private void logoutAction(ActionEvent e) {
        int choice = JOptionPane.showConfirmDialog(this, "Are you sure you want to logout?", "Logout", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            this.dispose(); 
            // فتح صفحة اللوج إن وتطبيق الأيقونة عليها أيضاً
            LoginFrame login = new LoginFrame(system);
            util.DBUtil.setWindowIcon(login);
            login.setVisible(true);
        }
    }
    
    private void showPanel(JPanel panelToShow) {
        contentPanel.removeAll();
        contentPanel.add(panelToShow, BorderLayout.CENTER);
        contentPanel.revalidate();
        contentPanel.repaint();
    }
}