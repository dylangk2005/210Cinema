
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PanelSanPham extends JPanel {
    // Form components
    private JTextField txtTenSanPham;
    private JTextField txtGiaSanPham;
    private JTextArea txtMoTa;
    private JTextField txtTimKiem;

    // Table
    private JTable table;
    private DefaultTableModel tableModel;

    // Buttons
    private JButton btnTimKiem;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXoaRong;

    public PanelSanPham() {
        initComponents();
        setupLayout();
        setupEvents();
        loadData();
    }

    private void initComponents() {
        // Form fields
        txtTenSanPham = new JTextField(20);
        txtGiaSanPham = new JTextField(20);
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);

        // Search field
        txtTimKiem = new JTextField(15);

        // Table
        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Giá", "Mô Tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit trực tiếp trên table
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Buttons
        btnTimKiem = new JButton("Tìm Kiếm");
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaRong = new JButton("Xóa Rỗng");
    }

    private void styleButton(JButton button, Color color) {
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setPreferredSize(new Dimension(100, 35));
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);

        // Top Panel - Form nhập liệu
        JPanel topPanel = createFormPanel();

        // Center Panel - Table
        JPanel centerPanel = createTablePanel();

        // Bottom Panel - Buttons
        JPanel bottomPanel = createButtonPanel();

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);
    }

    private JPanel createFormPanel() {
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Sản Phẩm"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Tên sản phẩm
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Tên Sản Phẩm:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtTenSanPham, gbc);

        // Giá sản phẩm
        gbc.gridx = 2; gbc.gridy = 0;
        formPanel.add(new JLabel("Giá:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtGiaSanPham, gbc);

        // Mô tả
        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        formPanel.add(new JLabel("Mô Tả:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.BOTH;
        formPanel.add(new JScrollPane(txtMoTa), gbc);

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh Sách Sản Phẩm"));
        tablePanel.setBackground(Color.WHITE);

        // Table with scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));

        tablePanel.add(scrollPane, BorderLayout.CENTER);

        return tablePanel;
    }

    private JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel(new BorderLayout());
        buttonPanel.setBackground(Color.WHITE);

        // Left side - Search
        JPanel leftPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftPanel.setBackground(Color.WHITE);
        leftPanel.add(new JLabel("Tìm theo mã:"));
        leftPanel.add(txtTimKiem);
        leftPanel.add(btnTimKiem);

        // Right side - Action buttons
        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightPanel.setBackground(Color.WHITE);
        rightPanel.add(btnThem);
        rightPanel.add(btnSua);
        rightPanel.add(btnXoa);
        rightPanel.add(btnXoaRong);

        buttonPanel.add(leftPanel, BorderLayout.WEST);
        buttonPanel.add(rightPanel, BorderLayout.EAST);

        return buttonPanel;
    }

    private void setupEvents() {
        // Table selection event
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });

        // Button events
        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnXoaRong.addActionListener(e -> clearForm());
        btnTimKiem.addActionListener(e -> timKiemSanPham());
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtTenSanPham.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtGiaSanPham.setText(tableModel.getValueAt(selectedRow, 2).toString());
            txtMoTa.setText(tableModel.getValueAt(selectedRow, 3).toString());
        }
    }

    private void themSanPham() {
        if (validateForm()) {
            String tenSP = txtTenSanPham.getText().trim();
            String gia = txtGiaSanPham.getText().trim();
            String moTa = txtMoTa.getText().trim();

            // Generate simple ID (in real app, get from database)
            String maSP = "SP" + String.format("%03d", tableModel.getRowCount() + 1);

            Object[] row = {maSP, tenSP, gia, moTa};
            tableModel.addRow(row);

            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
        }
    }

    private void suaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            if (validateForm()) {
                tableModel.setValueAt(txtTenSanPham.getText().trim(), selectedRow, 1);
                tableModel.setValueAt(txtGiaSanPham.getText().trim(), selectedRow, 2);
                tableModel.setValueAt(txtMoTa.getText().trim(), selectedRow, 3);

                JOptionPane.showMessageDialog(this, "Cập nhật sản phẩm thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần sửa!");
        }
    }

    private void xoaSanPham() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa sản phẩm này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa sản phẩm thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn sản phẩm cần xóa!");
        }
    }

    private void timKiemSanPham() {
        String maSP = txtTimKiem.getText().trim();
        if (maSP.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã sản phẩm cần tìm!");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(maSP)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                fillFormFromSelectedRow();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Không tìm thấy sản phẩm có mã: " + maSP);
    }

    private void clearForm() {
        txtTenSanPham.setText("");
        txtGiaSanPham.setText("");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }

    private boolean validateForm() {
        if (txtTenSanPham.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên sản phẩm!");
            txtTenSanPham.requestFocus();
            return false;
        }

        if (txtGiaSanPham.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập giá sản phẩm!");
            txtGiaSanPham.requestFocus();
            return false;
        }

        try {
            Double.parseDouble(txtGiaSanPham.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Giá sản phẩm phải là số!");
            txtGiaSanPham.requestFocus();
            return false;
        }

        return true;
    }

    private void loadData() {
        // Sample data - trong thực tế sẽ load từ database
        Object[][] sampleData = {
                {"SP001", "Nước ngọt Coca Cola", "15000", "Nước ngọt có ga vị cola"},
                {"SP002", "Bắp rang bơ", "25000", "Bắp rang bơ size M"},
                {"SP003", "Kẹo", "10000", "Kẹo ngọt các loại"},
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
}