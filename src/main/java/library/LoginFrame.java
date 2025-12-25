package library;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;

public class LoginFrame extends JFrame {
    
    private LibrarySystem system;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginFrame(LibrarySystem system) {
        this.system = system;
        
        setTitle("Library Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
        setLocationRelativeTo(null); 
        
        JPanel panel = new JPanel(new GridLayout(3, 2, 10, 10)); 
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20)); 
        
        // 1. حقل اسم المستخدم
        panel.add(new JLabel("Username:"));
        usernameField = new JTextField(15);
        // ✅ تم إزالة ahmed_mngr لتبقى الخانة فاضية
        panel.add(usernameField);
        
        // 2. حقل كلمة المرور
        panel.add(new JLabel("Password:"));
        passwordField = new JPasswordField(15);
        // ✅ تم إزالة 123 لتبقى الخانة فاضية
        panel.add(passwordField);
        
        panel.add(new JLabel("")); 
        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(loginButton);
        
        add(panel, BorderLayout.CENTER);
        
        loginButton.addActionListener(this::loginAction);
        getRootPane().setDefaultButton(loginButton);
    }
    
    private void loginAction(ActionEvent e) {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword()); 
        
        // التحقق من البيانات (دلوقتي بيقرأ من الـ System اللي مربوط بالـ Database)
        User loggedInUser = system.login(username, password);
        
        if (loggedInUser != null) {
            JOptionPane.showMessageDialog(this, "Welcome, " + loggedInUser.getName() + "!", "Login Success", JOptionPane.INFORMATION_MESSAGE);
            this.dispose();
            
            if (loggedInUser instanceof Admin) {
                new AdminDashboardFrame(system, (Admin) loggedInUser).setVisible(true);
            } else if (loggedInUser instanceof Member) {
                JOptionPane.showMessageDialog(null, "Member login successful. Dashboard coming soon.", "Next Step", JOptionPane.PLAIN_MESSAGE);
            }
            
        } else {
            JOptionPane.showMessageDialog(this, "Invalid Username or Password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
        }
    }
}