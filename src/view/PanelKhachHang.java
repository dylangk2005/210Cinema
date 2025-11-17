
package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PanelKhachHang extends JPanel {
    // Form components
    private JTextField txtHoTen;
    private JTextField txtNgaySinh;
    private JComboBox<String> cboGioiTinh;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JComboBox<String> cboHangThanhVien;
    private JTextField txtDiemTichLuy;
    private JTextField txtNgayDangKy;
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

    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat dateTimeFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");

    public PanelKhachHang() {
        initComponents();
        setupLayout();
        setupEvents();
        loadData();
    }

    private void initComponents() {
        // Form fields
        txtHoTen = new JTextField(20);
        txtNgaySinh = new JTextField(15);

        // Gender ComboBox
        cboGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});

        txtSoDienThoai = new JTextField(15);
        txtEmail = new JTextField(25);

        // Membership ComboBox
        cboHangThanhVien = new JComboBox<>(new String[]{"Bạc", "Vàng", "Bạch Kim", "Kim Cương"});

        txtDiemTichLuy = new JTextField(10);
        txtDiemTichLuy.setText("0");

        txtNgayDangKy = new JTextField(15);
        txtNgayDangKy.setText(dateTimeFormat.format(new Date()));

        // Search field
        txtTimKiem = new JTextField(15);

        // Table
        String[] columns = {"Mã KH", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT", "Email", "Hạng TV", "Điểm TL", "Ngày ĐK"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS
        );

        // Set column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(60);  // Mã KH
        table.getColumnModel().getColumn(1).setPreferredWidth(150); // Họ Tên
        table.getColumnModel().getColumn(2).setPreferredWidth(100); // Ngày Sinh
        table.getColumnModel().getColumn(3).setPreferredWidth(80);  // Giới Tính
        table.getColumnModel().getColumn(4).setPreferredWidth(100); // SĐT
        table.getColumnModel().getColumn(5).setPreferredWidth(200); // Email
        table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Hạng TV
        table.getColumnModel().getColumn(7).setPreferredWidth(70);  // Điểm TL
        table.getColumnModel().getColumn(8).setPreferredWidth(120); // Ngày ĐK

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
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông Tin Khách Hàng"));
        formPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.anchor = GridBagConstraints.WEST;

        // Row 1: Họ Tên + Ngày Sinh
        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(txtHoTen, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Ngày Sinh (dd/mm/yyyy):"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtNgaySinh, gbc);

        // Row 2: Giới Tính + SĐT
        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(new JLabel("Giới Tính:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboGioiTinh, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Số Điện Thoại:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtSoDienThoai, gbc);

        // Row 3: Email
        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        formPanel.add(txtEmail, gbc);

        // Row 4: Hạng Thành Viên + Điểm Tích Lũy
        gbc.gridx = 0; gbc.gridy = 3;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE;
        formPanel.add(new JLabel("Hạng Thành Viên:"), gbc);
        gbc.gridx = 1;
        formPanel.add(cboHangThanhVien, gbc);

        gbc.gridx = 2;
        formPanel.add(new JLabel("Điểm Tích Lũy:"), gbc);
        gbc.gridx = 3;
        formPanel.add(txtDiemTichLuy, gbc);

        // Row 5: Ngày Đăng Ký
        gbc.gridx = 0; gbc.gridy = 4;
        formPanel.add(new JLabel("Ngày Đăng Ký:"), gbc);
        gbc.gridx = 1;
        txtNgayDangKy.setEditable(false); // Auto generated
        formPanel.add(txtNgayDangKy, gbc);

        return formPanel;
    }

    private JPanel createTablePanel() {
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh Sách Khách Hàng"));
        tablePanel.setBackground(Color.WHITE);

        // Table with scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(0, 300));
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

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
        btnThem.addActionListener(e -> themKhachHang());
        btnSua.addActionListener(e -> suaKhachHang());
        btnXoa.addActionListener(e -> xoaKhachHang());
        btnXoaRong.addActionListener(e -> clearForm());
        btnTimKiem.addActionListener(e -> timKiemKhachHang());
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            txtHoTen.setText(tableModel.getValueAt(selectedRow, 1).toString());
            txtNgaySinh.setText(tableModel.getValueAt(selectedRow, 2).toString());
            cboGioiTinh.setSelectedItem(tableModel.getValueAt(selectedRow, 3).toString());
            txtSoDienThoai.setText(tableModel.getValueAt(selectedRow, 4).toString());
            txtEmail.setText(tableModel.getValueAt(selectedRow, 5).toString());
            cboHangThanhVien.setSelectedItem(tableModel.getValueAt(selectedRow, 6).toString());
            txtDiemTichLuy.setText(tableModel.getValueAt(selectedRow, 7).toString());
            txtNgayDangKy.setText(tableModel.getValueAt(selectedRow, 8).toString());
        }
    }

    private void themKhachHang() {
        if (validateForm()) {
            String hoTen = txtHoTen.getText().trim();
            String ngaySinh = txtNgaySinh.getText().trim();
            String gioiTinh = cboGioiTinh.getSelectedItem().toString();
            String sdt = txtSoDienThoai.getText().trim();
            String email = txtEmail.getText().trim();
            String hangTV = cboHangThanhVien.getSelectedItem().toString();
            String diemTL = txtDiemTichLuy.getText().trim();
            String ngayDK = txtNgayDangKy.getText().trim();

            // Generate simple ID (in real app, get from database)
            String maKH = "KH" + String.format("%03d", tableModel.getRowCount() + 1);

            Object[] row = {maKH, hoTen, ngaySinh, gioiTinh, sdt, email, hangTV, diemTL, ngayDK};
            tableModel.addRow(row);

            clearForm();
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công!");
        }
    }

    private void suaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            if (validateForm()) {
                tableModel.setValueAt(txtHoTen.getText().trim(), selectedRow, 1);
                tableModel.setValueAt(txtNgaySinh.getText().trim(), selectedRow, 2);
                tableModel.setValueAt(cboGioiTinh.getSelectedItem().toString(), selectedRow, 3);
                tableModel.setValueAt(txtSoDienThoai.getText().trim(), selectedRow, 4);
                tableModel.setValueAt(txtEmail.getText().trim(), selectedRow, 5);
                tableModel.setValueAt(cboHangThanhVien.getSelectedItem().toString(), selectedRow, 6);
                tableModel.setValueAt(txtDiemTichLuy.getText().trim(), selectedRow, 7);
                tableModel.setValueAt(txtNgayDangKy.getText().trim(), selectedRow, 8);

                JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần sửa!");
        }
    }

    private void xoaKhachHang() {
        int selectedRow = table.getSelectedRow();
        if (selectedRow >= 0) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Bạn có chắc chắn muốn xóa khách hàng này?",
                    "Xác nhận",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                tableModel.removeRow(selectedRow);
                clearForm();
                JOptionPane.showMessageDialog(this, "Xóa khách hàng thành công!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn khách hàng cần xóa!");
        }
    }

    private void timKiemKhachHang() {
        String maKH = txtTimKiem.getText().trim();
        if (maKH.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã khách hàng cần tìm!");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (tableModel.getValueAt(i, 0).toString().equalsIgnoreCase(maKH)) {
                table.setRowSelectionInterval(i, i);
                table.scrollRectToVisible(table.getCellRect(i, 0, true));
                fillFormFromSelectedRow();
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Không tìm thấy khách hàng có mã: " + maKH);
    }

    private void clearForm() {
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        cboGioiTinh.setSelectedIndex(0);
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        cboHangThanhVien.setSelectedIndex(0);
        txtDiemTichLuy.setText("0");
        txtNgayDangKy.setText(dateTimeFormat.format(new Date()));
        txtTimKiem.setText("");
        table.clearSelection();
    }

    private boolean validateForm() {
        // Validate Họ Tên
        if (txtHoTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập họ tên khách hàng!");
            txtHoTen.requestFocus();
            return false;
        }

        // Validate Ngày Sinh
        if (txtNgaySinh.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập ngày sinh!");
            txtNgaySinh.requestFocus();
            return false;
        }

        try {
            dateFormat.parse(txtNgaySinh.getText().trim());
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày sinh không đúng! (dd/MM/yyyy)");
            txtNgaySinh.requestFocus();
            return false;
        }

        // Validate SĐT
        if (txtSoDienThoai.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập số điện thoại!");
            txtSoDienThoai.requestFocus();
            return false;
        }

        // Validate Email
        if (!txtEmail.getText().trim().isEmpty()) {
            String email = txtEmail.getText().trim();
            if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
                JOptionPane.showMessageDialog(this, "Định dạng email không đúng!");
                txtEmail.requestFocus();
                return false;
            }
        }

        // Validate Điểm Tích Lũy
        try {
            Integer.parseInt(txtDiemTichLuy.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Điểm tích lũy phải là số!");
            txtDiemTichLuy.requestFocus();
            return false;
        }

        return true;
    }

    private void loadData() {
        // Sample data - trong thực tế sẽ load từ database
        Object[][] sampleData = {
                {"KH001", "Nguyễn Văn An", "15/05/1990", "Nam", "0901234567", "an.nguyen@email.com", "Vàng", "150", "01/01/2023 10:30"},
                {"KH002", "Trần Thị Bình", "20/08/1985", "Nữ", "0987654321", "binh.tran@email.com", "Bạch Kim", "300", "15/02/2023 14:20"},
                {"KH003", "Lê Văn Cường", "10/12/1995", "Nam", "0976543210", "cuong.le@email.com", "Bạc", "50", "20/03/2023 09:15"},
        };

        for (Object[] row : sampleData) {
            tableModel.addRow(row);
        }
    }
}