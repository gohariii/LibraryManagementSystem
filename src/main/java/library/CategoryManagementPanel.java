package library;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import dao.CategoryDAO;

public class CategoryManagementPanel extends JPanel {
    private JTable table;
    private DefaultTableModel model;

    public CategoryManagementPanel() {
        setLayout(new BorderLayout(10, 10));

        // الجزء العلوي: العنوان
        JPanel northPanel = new JPanel(new BorderLayout());
        JLabel headerLabel = new JLabel("Categories Management", SwingConstants.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24)); 
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        northPanel.add(headerLabel, BorderLayout.CENTER);
        add(northPanel, BorderLayout.NORTH);

        // إعداد الجدول ليشمل عمود الوصف
        model = new DefaultTableModel(new String[]{"ID", "Category Name", "Description"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        // الأزرار
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JButton addBtn = new JButton("Add Category");
        JButton delBtn = new JButton("Delete Category");

        // لوجيك الإضافة الجديد بخانتين
        addBtn.addActionListener(e -> {
            JTextField nameField = new JTextField(15);
            JTextField descField = new JTextField(15);

            JPanel inputPanel = new JPanel(new GridLayout(0, 1, 5, 5));
            inputPanel.add(new JLabel("Category Name:"));
            inputPanel.add(nameField);
            inputPanel.add(new JLabel("Description (Optional):"));
            inputPanel.add(descField);

            int result = JOptionPane.showConfirmDialog(this, inputPanel, 
                    "Add New Category", JOptionPane.OK_CANCEL_OPTION);
            
            if (result == JOptionPane.OK_OPTION) {
                String name = nameField.getText().trim();
                String desc = descField.getText().trim();

                if (!name.isEmpty()) {
                    if (CategoryDAO.addCategory(name, desc)) { 
                        loadData(); 
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Category Name is required!");
                }
            }
        });

        delBtn.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row != -1) {
                int id = (int) model.getValueAt(row, 0);
                if (JOptionPane.showConfirmDialog(this, "Delete this category?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    if (CategoryDAO.deleteCategory(id)) { 
                        loadData(); 
                    } else { 
                        JOptionPane.showMessageDialog(this, "Cannot delete! Linked to books."); 
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Please select a category first.");
            }
        });

        btnPanel.add(addBtn); 
        btnPanel.add(delBtn);
        add(btnPanel, BorderLayout.SOUTH);
        
        loadData();
    }

    private void loadData() {
        model.setRowCount(0);
        // عرض المعطيات الثلاثة في الجدول
        CategoryDAO.getAllCategories().forEach(c -> 
            model.addRow(new Object[]{c.getId(), c.getName(), c.getDescription()})
        );
    }
}