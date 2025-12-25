package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import dao.MemberDAO;

public class MemberManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public MemberManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        // --- الجزء العلوي المعدل ---
        JPanel northPanel = new JPanel(new BorderLayout());
        
        // تكبير الفونت وإضافة مسافة (Padding)
        JLabel headerLabel = new JLabel("Member Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24)); // ✅ فونت كبير وواضح
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        searchField = new JTextField(20);
        searchField.setToolTipText("Search by: Member Name, ID, or Username"); // ✅ الصندوق الأصفر
        
        JPanel searchBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchBar.add(new JLabel("Search Member: "));
        searchBar.add(searchField);
        
        northPanel.add(headerLabel, BorderLayout.NORTH);
        northPanel.add(searchBar, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(new String[]{"ID", "Name", "Username", "Email"}, 0);
        table = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(sorter);
        add(new JScrollPane(table), BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { filter(); }
            public void removeUpdate(DocumentEvent e) { filter(); }
            public void changedUpdate(DocumentEvent e) { filter(); }
            private void filter() {
                String text = searchField.getText();
                // البحث في: ID, Name, Username
                sorter.setRowFilter(text.trim().isEmpty() ? null : RowFilter.regexFilter("(?i)" + text, 0, 1, 2));
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JButton addBtn = new JButton("Add Member");
        JButton editBtn = new JButton("Edit Selected");
        JButton delBtn = new JButton("Delete Member");

        addBtn.addActionListener(e -> new AddMemberFrame(this, null));
        editBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                Member m = new Member(
                    (int)tableModel.getValueAt(modelRow, 0),
                    (String)tableModel.getValueAt(modelRow, 1),
                    (String)tableModel.getValueAt(modelRow, 2),
                    (String)tableModel.getValueAt(modelRow, 3)
                );
                new AddMemberFrame(this, m);
            }
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                int id = (int) tableModel.getValueAt(modelRow, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete Member?") == JOptionPane.YES_OPTION) {
                    if (MemberDAO.deleteMember(id)) loadMembersData();
                }
            }
        });

        btnPanel.add(addBtn); btnPanel.add(editBtn); btnPanel.add(delBtn);
        add(btnPanel, BorderLayout.SOUTH);
        loadMembersData();
    }

    public void loadMembersData() {
        tableModel.setRowCount(0);
        List<Member> members = MemberDAO.getAllMembers();
        for (Member m : members) tableModel.addRow(new Object[]{m.getId(), m.getName(), m.getUsername(), m.getEmail()});
    }
}