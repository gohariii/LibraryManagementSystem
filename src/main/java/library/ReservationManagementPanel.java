package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import dao.ReservationDAO;

public class ReservationManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel tableModel;
    private List<Reservation> currentData;
    private JTextField searchField;
    private TableRowSorter<DefaultTableModel> sorter;

    public ReservationManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        JLabel headerLabel = new JLabel("Pending Reservations Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 22));
        northPanel.add(headerLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(25);
        
        // ✅ إضافة الصندوق الأصفر (ToolTip)
        searchField.setToolTipText("Search by: Reservation ID, Member ID, Name, or Book Title");
        
        searchPanel.add(searchField);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        String[] columns = {"Res ID", "Member Name", "Member ID", "Book Title", "Reservation Date"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
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
                if (text.trim().isEmpty()) sorter.setRowFilter(null);
                // ✅ البحث في: Res ID (0), Member Name (1), Member ID (2), Book Title (3)
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1, 2, 3));
            }
        });

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 15));
        JButton confirmBtn = new JButton("Confirm & Issue Loan");
        JButton cancelBtn = new JButton("Cancel Reservation");
        JButton refreshBtn = new JButton("Refresh List");

        confirmBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int modelRow = table.convertRowIndexToModel(row);
                Reservation res = currentData.get(modelRow);
                if (ReservationDAO.confirmAndIssue(res.getId(), res.getBook().getId(), res.getMember().getId())) {
                    JOptionPane.showMessageDialog(this, "Loan issued!"); loadData();
                }
            }
        });

        cancelBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) tableModel.getValueAt(table.convertRowIndexToModel(row), 0);
                if (ReservationDAO.deleteReservation(id)) { loadData(); }
            }
        });

        refreshBtn.addActionListener(e -> loadData());
        btnPanel.add(confirmBtn); btnPanel.add(cancelBtn); btnPanel.add(refreshBtn);
        add(btnPanel, BorderLayout.SOUTH);
        loadData();
    }

    public void loadData() {
        tableModel.setRowCount(0);
        currentData = ReservationDAO.getAllReservations();
        if (currentData != null) {
            for (Reservation r : currentData) {
                tableModel.addRow(new Object[]{ r.getId(), r.getMember().getName(), r.getMember().getId(), r.getBook().getTitle(), r.getReservationDate() });
            }
        }
    }
}