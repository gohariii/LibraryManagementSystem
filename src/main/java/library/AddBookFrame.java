package library;

import dao.BookDAO;
import dao.CategoryDAO;
import javax.swing.*;
import java.awt.*;
import java.util.List;
import util.DBUtil;

public class AddBookFrame extends JFrame {
    private JTextField titleF, authorF, isbnF, yearF, totalF;
    private JComboBox<Category> catCombo;
    private BookManagementPanel parent;
    private Book editingBook = null;

    public AddBookFrame(BookManagementPanel parent, Book book) {
        this.parent = parent;
        this.editingBook = book;
        
        setTitle(book == null ? "Add New Book" : "Edit Book");
        setSize(450, 500);
        util.DBUtil.setWindowIcon(this);
        setLayout(new BorderLayout(10, 10));

        JPanel fieldsPanel = new JPanel(new GridLayout(7, 2, 10, 10));
        fieldsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        fieldsPanel.add(new JLabel("Book Title:")); titleF = new JTextField(); fieldsPanel.add(titleF);
        fieldsPanel.add(new JLabel("Author Name:")); authorF = new JTextField(); fieldsPanel.add(authorF);
        fieldsPanel.add(new JLabel("ISBN:")); isbnF = new JTextField(); fieldsPanel.add(isbnF);
        fieldsPanel.add(new JLabel("Year:")); yearF = new JTextField(); fieldsPanel.add(yearF);
        fieldsPanel.add(new JLabel("Total Copies:")); totalF = new JTextField(); fieldsPanel.add(totalF);
        fieldsPanel.add(new JLabel("Category:")); catCombo = new JComboBox<>(); fieldsPanel.add(catCombo);

        List<Category> categories = CategoryDAO.getAllCategories();
        catCombo.addItem(new Category(0, "No Category"));
        for (Category c : categories) catCombo.addItem(c);

        if (book != null) {
            titleF.setText(book.getTitle());
            authorF.setText(book.getAuthor());
            isbnF.setText(book.getIsbn());
            yearF.setText(String.valueOf(book.getPublishedYear()));
            totalF.setText(String.valueOf(book.getTotalCopies()));
        }

        add(fieldsPanel, BorderLayout.CENTER);

        // زرار بيزك تماماً
        JButton saveBtn = new JButton(book == null ? "Add Book" : "Save Changes");
        saveBtn.addActionListener(e -> saveAction());
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(saveBtn);
        add(btnPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void saveAction() {
        try {
            String title = titleF.getText().trim();
            if (title.isEmpty()) return;

            if (editingBook == null) {
                Book newBook = new Book(0, title, authorF.getText(), isbnF.getText(), (Category)catCombo.getSelectedItem(), Integer.parseInt(yearF.getText()), Integer.parseInt(totalF.getText()), Integer.parseInt(totalF.getText()), 0);
                if (BookDAO.addBook(newBook)) { parent.loadBooksData(); dispose(); }
            } else {
                Book updated = new Book(editingBook.getId(), title, authorF.getText(), isbnF.getText(), (Category)catCombo.getSelectedItem(), Integer.parseInt(yearF.getText()), Integer.parseInt(totalF.getText()), editingBook.getAvailableCopies(), editingBook.getReservedCount());
                if (BookDAO.updateBook(updated)) { parent.loadBooksData(); dispose(); }
            }
        } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Check inputs!"); }
    }
}