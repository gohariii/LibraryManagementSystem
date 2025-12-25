package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import dao.LoanDAO;

public class LoanManagementPanel extends JPanel {
    private JTable loanTable;
    private DefaultTableModel tableModel;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public LoanManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        JLabel label = new JLabel("Library Loan Management", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 22));
        northPanel.add(label, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(20);
        
        // ✅ إضافة الصندوق الأصفر (ToolTip)
        searchField.setToolTipText("Search by: Loan ID, Member ID, Member Name, or Book Title");
        
        searchPanel.add(searchField);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        String[] columns = {"Loan ID", "Mem ID", "Member Name", "Book ID", "Book Title", "Issue Date", "Due Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        
        loanTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        loanTable.setRowSorter(sorter);
        add(new JScrollPane(loanTable), BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
            private void search() {
                String text = searchField.getText();
                if (text.trim().isEmpty()) sorter.setRowFilter(null);
                // ✅ البحث في: Loan ID (0), Member ID (1), Member Name (2), Book Title (4)
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1, 2, 4));
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton issueBtn = new JButton("Issue New Loan");
        JButton returnBtn = new JButton("Return Book");
        JButton refreshBtn = new JButton("Refresh Table");

        issueBtn.addActionListener(e -> {
            String bId = JOptionPane.showInputDialog(this, "Book ID:");
            String mId = JOptionPane.showInputDialog(this, "Member ID:");
            if (bId != null && mId != null) {
                if (LoanDAO.issueLoan(Integer.parseInt(bId), Integer.parseInt(mId))) {
                    JOptionPane.showMessageDialog(this, "Success!"); loadLoanData();
                }
            }
        });

        returnBtn.addActionListener(e -> {
            int row = loanTable.getSelectedRow();
            if (row != -1) {
                int modelRow = loanTable.convertRowIndexToModel(row);
                executeReturn((int)tableModel.getValueAt(modelRow, 0), (int)tableModel.getValueAt(modelRow, 3));
            }
        });

        refreshBtn.addActionListener(e -> loadLoanData());
        btnPanel.add(issueBtn); btnPanel.add(returnBtn); btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);
        loadLoanData(); 
    }

    private void executeReturn(int lId, int bId) {
        if (LoanDAO.returnLoan(lId, bId)) { JOptionPane.showMessageDialog(this, "Returned!"); loadLoanData(); }
    }

    public void loadLoanData() {
        tableModel.setRowCount(0);
        List<Object[]> loans = LoanDAO.getAllActiveLoans();
        for (Object[] loan : loans) tableModel.addRow(loan);
    }
}