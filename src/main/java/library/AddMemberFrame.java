package library;

import javax.swing.*;
import java.awt.*;
import dao.MemberDAO;

public class AddMemberFrame extends JFrame {
    private JTextField nameF, userF, emailF;
    private MemberManagementPanel parent;
    private Member editingMember;

    public AddMemberFrame(MemberManagementPanel parent, Member member) {
        this.parent = parent;
        this.editingMember = member;
        setTitle(member == null ? "Add Member" : "Edit Member");
        setSize(350, 250);
        setLayout(new GridLayout(4, 2, 10, 10));
        setLocationRelativeTo(null);

        nameF = new JTextField(); userF = new JTextField(); emailF = new JTextField();
        
        if (member != null) {
            nameF.setText(member.getName()); 
            userF.setText(member.getUsername()); 
            emailF.setText(member.getEmail()); 
            userF.setEditable(false);
        }

        add(new JLabel(" Name:")); add(nameF);
        add(new JLabel(" Username:")); add(userF);
        add(new JLabel(" Email:")); add(emailF);

        JButton saveBtn = new JButton("Save");
        saveBtn.addActionListener(e -> {
            if (editingMember == null) {
                if (MemberDAO.addMember(nameF.getText(), userF.getText(), emailF.getText())) {
                    parent.loadMembersData(); dispose();
                } else { JOptionPane.showMessageDialog(this, "Database Error!"); }
            } else {
                editingMember.setName(nameF.getText());
                editingMember.setEmail(emailF.getText());
                if (MemberDAO.updateMember(editingMember)) {
                    parent.loadMembersData(); dispose();
                } else { JOptionPane.showMessageDialog(this, "Update Failed!"); }
            }
        });
        
        add(new JLabel("")); add(saveBtn);
        setVisible(true);
    }
}