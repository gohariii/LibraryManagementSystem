package library;

import dao.BookDAO;
import dao.ReservationDAO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;

public class BookManagementPanel extends JPanel {
    private JTable bookTable;
    private DefaultTableModel tableModel;
    private JTextField searchField; 
    private TableRowSorter<DefaultTableModel> sorter; 

    public BookManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        JPanel northPanel = new JPanel(new BorderLayout(5, 5));
        JLabel titleLabel = new JLabel("Books Inventory Management", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        northPanel.add(titleLabel, BorderLayout.NORTH);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        searchPanel.add(new JLabel("Search: "));
        searchField = new JTextField(25);
        
        // ✅ إضافة الصندوق الأصفر (ToolTip)
        searchField.setToolTipText("Search by: Book ID, Title, Author, or ISBN");
        
        searchPanel.add(searchField);
        northPanel.add(searchPanel, BorderLayout.SOUTH);
        add(northPanel, BorderLayout.NORTH);

        tableModel = new DefaultTableModel(
            new String[]{"ID", "Title", "Author", "ISBN", "Category", "Year", "Total", "Available", "Reserved"}, 0
        ) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        bookTable = new JTable(tableModel);
        sorter = new TableRowSorter<>(tableModel);
        bookTable.setRowSorter(sorter);
        add(new JScrollPane(bookTable), BorderLayout.CENTER);

        searchField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { search(); }
            public void removeUpdate(DocumentEvent e) { search(); }
            public void changedUpdate(DocumentEvent e) { search(); }
            private void search() {
                String text = searchField.getText();
                if (text.trim().isEmpty()) sorter.setRowFilter(null);
                // ✅ البحث في: ID (0), Title (1), Author (2), ISBN (3)
                else sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 0, 1, 2, 3));
            }
        });

        JPanel btnPanel = new JPanel();
        JButton addBtn = new JButton("Add New Book");
        JButton editBtn = new JButton("Edit Selected");
        JButton delBtn = new JButton("Delete Book");
        JButton reserveBtn = new JButton("Reserve Book");

        addBtn.addActionListener(e -> new AddBookFrame(this, null)); 
        
        editBtn.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row != -1) {
                int modelRow = bookTable.convertRowIndexToModel(row);
                int id = (int) tableModel.getValueAt(modelRow, 0);
                for (Book b : BookDAO.getAllBooks()) {
                    if (b.getId() == id) { new AddBookFrame(this, b); break; }
                }
            }
        });

        delBtn.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row != -1) {
                int modelRow = bookTable.convertRowIndexToModel(row);
                int id = (int) tableModel.getValueAt(modelRow, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete book?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (BookDAO.deleteBook(id)) { loadBooksData(); }
                }
            }
        });

        reserveBtn.addActionListener(e -> {
            int row = bookTable.getSelectedRow();
            if (row != -1) {
                int modelRow = bookTable.convertRowIndexToModel(row);
                int bookId = (int) tableModel.getValueAt(modelRow, 0);
                int available = (int) tableModel.getValueAt(modelRow, 7);
                if (available <= 0) { JOptionPane.showMessageDialog(this, "No available copies!"); return; }
                String mIdStr = JOptionPane.showInputDialog(this, "Enter Member ID:");
                if (mIdStr != null && !mIdStr.trim().isEmpty()) {
                    try {
                        if (ReservationDAO.addReservation(bookId, Integer.parseInt(mIdStr.trim()))) {
                            JOptionPane.showMessageDialog(this, "Reserved!"); loadBooksData();
                        }
                    } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Invalid ID"); }
                }
            }
        });

        btnPanel.add(addBtn); btnPanel.add(editBtn); btnPanel.add(delBtn); btnPanel.add(reserveBtn);
        add(btnPanel, BorderLayout.SOUTH);
        loadBooksData();
    }

    public void loadBooksData() {
        tableModel.setRowCount(0);
        List<Book> books = BookDAO.getAllBooks();
        for (Book b : books) {
            tableModel.addRow(new Object[]{ b.getId(), b.getTitle(), b.getAuthor(), b.getIsbn(), (b.getCategory() != null) ? b.getCategory().getName() : "N/A", b.getPublishedYear(), b.getTotalCopies(), b.getAvailableCopies(), b.getReservedCount() });
        }
    }
}