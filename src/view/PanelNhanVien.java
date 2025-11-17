package view;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;

public class PanelNhanVien extends JPanel {

    private JTextField txtMaNV, txtHoTen, txtEmail, txtDiaChi, txtSDT, txtSearch;
    private JComboBox<String> cbChucVu;
    private JRadioButton rdNam, rdNu, rdDangLam, rdNghi;
    private JTable table;
    private DefaultTableModel model;

    public PanelNhanVien() {

        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        // =======================================================================
        // 1) PANEL CHỈNH SỬA HỒ SƠ
        // =======================================================================

        JPanel panelEdit = new JPanel(new BorderLayout());
        panelEdit.setBackground(Color.WHITE);
        panelEdit.setBorder(new TitledBorder(new LineBorder(new Color(0, 70, 140), 2),
                "CHỈNH SỬA HỒ SƠ",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 20),
                new Color(0, 70, 140)));

        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(7, 10, 7, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMaNV = new JTextField(20);
        txtHoTen = new JTextField(20);
        txtEmail = new JTextField(20);
        txtDiaChi = new JTextField(20);
        txtSDT = new JTextField(20);
        cbChucVu = new JComboBox<>(new String[]{"Nhân viên", "Quản lý"});

        rdNam = new JRadioButton("Nam");
        rdNu = new JRadioButton("Nữ");
        ButtonGroup bgGender = new ButtonGroup();
        bgGender.add(rdNam);
        bgGender.add(rdNu);
        rdNam.setOpaque(false);
        rdNu.setOpaque(false);

        rdDangLam = new JRadioButton("Đang làm");
        rdNghi = new JRadioButton("Nghỉ");
        ButtonGroup bgStatus = new ButtonGroup();
        bgStatus.add(rdDangLam);
        bgStatus.add(rdNghi);
        rdDangLam.setOpaque(false);
        rdNghi.setOpaque(false);

        // ==== dòng 1 ====
        addRow(form, gbc, 0, "Mã nhân viên:", txtMaNV, "Số điện thoại:", txtSDT);

        // ==== dòng 2 ====
        addRow(form, gbc, 1, "Họ và Tên:", txtHoTen, "Trạng thái:", createStatusPanel());

        // ==== dòng 3 ====
        addRow(form, gbc, 2, "Email:", txtEmail, "Chức vụ:", cbChucVu);

        // ==== dòng 4 ====
        addRow(form, gbc, 3, "Địa chỉ:", txtDiaChi, "Giới tính:", createGenderPanel());

        // ==== nút chức năng ====
        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 15, 10));
        btnPanel.setBackground(Color.WHITE);

        JButton btnThem = new JButton("Thêm");
        JButton btnClear = new JButton("Xóa rỗng");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        txtSearch = new JTextField(15);
        JButton btnTim = new JButton("Tìm");

        btnPanel.add(btnThem);
        btnPanel.add(btnClear);
        btnPanel.add(btnSua);
        btnPanel.add(btnXoa);
        btnPanel.add(new JLabel("Nhập mã cần tìm:"));
        btnPanel.add(txtSearch);
        btnPanel.add(btnTim);

        // Thêm vào panelEdit
        panelEdit.add(form, BorderLayout.CENTER);
        panelEdit.add(btnPanel, BorderLayout.SOUTH);

        // =======================================================================
        // 2) PANEL DANH SÁCH NHÂN VIÊN
        // =======================================================================

        JPanel panelList = new JPanel(new BorderLayout());
        panelList.setBorder(new TitledBorder(new LineBorder(new Color(0, 70, 140), 2),
                "DANH SÁCH NHÂN VIÊN",
                TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 20),
                new Color(0, 70, 140)));
        panelList.setBackground(Color.WHITE);

        String[] cols = {
                "Mã NV", "Họ tên", "Chức vụ", "Giới tính", "SĐT",
                "Email", "Địa chỉ", "Trạng thái"
        };

        Object[][] data = {
                {"NV001", "Nguyễn Văn An", "Quản lý", "Nam", "0891192365", "an.nguyen@example.com", "Q1, TP.HCM", "Đang làm"},
                {"NV002", "Hoàng Hùng Đào", "Nhân viên", "Nữ", "0944156788", "dao@example.com", "Q3, TP.HCM", "Đang làm"},
        };

        model = new DefaultTableModel(data, cols);
        table = new JTable(model);

        panelList.add(new JScrollPane(table), BorderLayout.CENTER);

        // =======================================================================
        // ADD 2 PANEL CHÍNH
        // =======================================================================
        add(panelEdit, BorderLayout.NORTH);
        add(panelList, BorderLayout.CENTER);

        // =======================================================================
        // XỬ LÝ SỰ KIỆN
        // =======================================================================

        // Click bảng → load dữ liệu
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = table.getSelectedRow();
                if (row >= 0)
                    loadToForm(row);
            }
        });

        // Xóa rỗng
        btnClear.addActionListener(e -> clearForm());

        // Thêm
        btnThem.addActionListener(e -> addEmployee());

        // Xóa
        btnXoa.addActionListener(e -> deleteEmployee());

        // Sửa
        btnSua.addActionListener(e -> updateEmployee());

        // Tìm
        btnTim.addActionListener(e -> searchEmployee());
    }

    // ========================================================================
    // HÀM TẠO FORM
    // ========================================================================

    private JPanel createGenderPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        p.add(rdNam);
        p.add(rdNu);
        return p;
    }

    private JPanel createStatusPanel() {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT));
        p.setOpaque(false);
        p.add(rdDangLam);
        p.add(rdNghi);
        return p;
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row,
                        String lb1, JComponent comp1,
                        String lb2, JComponent comp2) {

        gbc.gridy = row;

        gbc.gridx = 0;
        panel.add(new JLabel(lb1), gbc);

        gbc.gridx = 1;
        panel.add(comp1, gbc);

        gbc.gridx = 2;
        panel.add(new JLabel(lb2), gbc);

        gbc.gridx = 3;
        panel.add(comp2, gbc);
    }

    // ========================================================================
    // CHỨC NĂNG
    // ========================================================================

    private void clearForm() {
        txtMaNV.setText("");
        txtHoTen.setText("");
        txtEmail.setText("");
        txtDiaChi.setText("");
        txtSDT.setText("");
        rdNam.setSelected(false);
        rdNu.setSelected(false);
        rdDangLam.setSelected(false);
        rdNghi.setSelected(false);
        cbChucVu.setSelectedIndex(0);
    }

    private void loadToForm(int row) {

        txtMaNV.setText(model.getValueAt(row, 0).toString());
        txtHoTen.setText(model.getValueAt(row, 1).toString());
        cbChucVu.setSelectedItem(model.getValueAt(row, 2).toString());

        String gender = model.getValueAt(row, 3).toString();
        rdNam.setSelected(gender.equals("Nam"));
        rdNu.setSelected(gender.equals("Nữ"));

        txtSDT.setText(model.getValueAt(row, 4).toString());
        txtEmail.setText(model.getValueAt(row, 5).toString());
        txtDiaChi.setText(model.getValueAt(row, 6).toString());

        String status = model.getValueAt(row, 7).toString();
        rdDangLam.setSelected(status.equals("Đang làm"));
        rdNghi.setSelected(status.equals("Nghỉ"));
    }

    private void addEmployee() {
        model.addRow(new Object[]{
                txtMaNV.getText(),
                txtHoTen.getText(),
                cbChucVu.getSelectedItem(),
                rdNam.isSelected() ? "Nam" : "Nữ",
                txtSDT.getText(),
                txtEmail.getText(),
                txtDiaChi.getText(),
                rdDangLam.isSelected() ? "Đang làm" : "Nghỉ"
        });
        JOptionPane.showMessageDialog(this, "Thêm nhân viên thành công!");
    }

    private void deleteEmployee() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            model.removeRow(row);
            JOptionPane.showMessageDialog(this, "Xóa thành công!");
        }
    }

    private void updateEmployee() {
        int row = table.getSelectedRow();
        if (row >= 0) {
            model.setValueAt(txtMaNV.getText(), row, 0);
            model.setValueAt(txtHoTen.getText(), row, 1);
            model.setValueAt(cbChucVu.getSelectedItem(), row, 2);
            model.setValueAt(rdNam.isSelected() ? "Nam" : "Nữ", row, 3);
            model.setValueAt(txtSDT.getText(), row, 4);
            model.setValueAt(txtEmail.getText(), row, 5);
            model.setValueAt(txtDiaChi.getText(), row, 6);
            model.setValueAt(rdDangLam.isSelected() ? "Đang làm" : "Nghỉ", row, 7);

            JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
        }
    }

    private void searchEmployee() {
        String key = txtSearch.getText().trim();
        for (int i = 0; i < model.getRowCount(); i++) {
            if (model.getValueAt(i, 0).toString().equalsIgnoreCase(key)) {
                table.setRowSelectionInterval(i, i);
                loadToForm(i);
                return;
            }
        }
        JOptionPane.showMessageDialog(this, "Không tìm thấy!");
    }
}
