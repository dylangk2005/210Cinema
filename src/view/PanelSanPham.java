package view;

import dao.SanPhamDAO;
import model.SanPham;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PanelSanPham extends JPanel {

    private JTextField txtTenSanPham;
    private JTextField txtGiaSanPham;
    private JTextArea txtMoTa;
    private JTextField txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnTimKiem;
    private JButton btnThem;
    private JButton btnSua;
    private JButton btnXoa;
    private JButton btnXoaRong;

    private SanPhamDAO dao;

    public PanelSanPham() {
        dao = new SanPhamDAO();
        initComponents();
        setupLayout();
        setupEvents();
        loadData();
    }

    private void initComponents() {
        txtTenSanPham = new JTextField(20);
        txtGiaSanPham = new JTextField(12);
        txtMoTa = new JTextArea(4, 20);
        txtMoTa.setLineWrap(true);
        txtMoTa.setWrapStyleWord(true);
        txtTimKiem = new JTextField(15);

        String[] columns = {"Mã SP", "Tên Sản Phẩm", "Đơn giá", "Mô tả"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnTimKiem = new JButton("Tìm Kiếm");
        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaRong = new JButton("Xóa Rỗng");

        // style (tuỳ bạn)
        btnThem.setBackground(new Color(0, 120, 215));
        btnThem.setForeground(Color.WHITE);
        btnSua.setBackground(new Color(0, 150, 50));
        btnSua.setForeground(Color.WHITE);
        btnXoa.setBackground(new Color(200, 30, 30));
        btnXoa.setForeground(Color.WHITE);
        btnXoaRong.setBackground(new Color(100, 100, 100));
        btnXoaRong.setForeground(Color.WHITE);
        btnTimKiem.setBackground(new Color(60, 60, 60));
        btnTimKiem.setForeground(Color.WHITE);
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(12, 12, 12, 12));
        setBackground(Color.WHITE);

        // form panel
        JPanel form = new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        form.add(new JLabel("Tên SP:"), gbc);
        gbc.gridx = 1; form.add(txtTenSanPham, gbc);

        gbc.gridx = 2; form.add(new JLabel("Đơn giá:"), gbc);
        gbc.gridx = 3; form.add(txtGiaSanPham, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        gbc.anchor = GridBagConstraints.NORTHWEST;
        form.add(new JLabel("Mô tả:"), gbc);
        gbc.gridx = 1; gbc.gridwidth = 3; gbc.fill = GridBagConstraints.BOTH;
        form.add(new JScrollPane(txtMoTa), gbc);

        add(form, BorderLayout.NORTH);

        // table panel
        JPanel tablePanel = new JPanel(new BorderLayout());
        tablePanel.setBorder(BorderFactory.createTitledBorder("Danh sách sản phẩm"));
        tablePanel.setBackground(Color.WHITE);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);
        add(tablePanel, BorderLayout.CENTER);

        // buttons panel
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        left.add(new JLabel("Tìm (mã/tên):"));
        left.add(txtTimKiem);
        left.add(btnTimKiem);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(btnThem);
        right.add(btnSua);
        right.add(btnXoa);
        right.add(btnXoaRong);

        bottom.add(left, BorderLayout.WEST);
        bottom.add(right, BorderLayout.EAST);

        add(bottom, BorderLayout.SOUTH);
    }

    private void setupEvents() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillFormFromSelectedRow();
        });

        btnThem.addActionListener(e -> themSanPham());
        btnSua.addActionListener(e -> suaSanPham());
        btnXoa.addActionListener(e -> xoaSanPham());
        btnXoaRong.addActionListener(e -> clearForm());
        btnTimKiem.addActionListener(e -> timKiemSanPham());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<SanPham> list = dao.getAll();
        for (SanPham sp : list) {
            tableModel.addRow(new Object[] {
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getDonGia() != null ? sp.getDonGia().toString() : "0",
                    sp.getMoTa()
            });
        }
    }

    private void fillFormFromSelectedRow() {
        int r = table.getSelectedRow();
        if (r < 0) return;
        txtTenSanPham.setText(tableModel.getValueAt(r,1).toString());
        txtGiaSanPham.setText(tableModel.getValueAt(r,2).toString());
        txtMoTa.setText(tableModel.getValueAt(r,3) == null ? "" : tableModel.getValueAt(r,3).toString());
    }

    private boolean validateForm() {
        if (txtTenSanPham.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập tên sản phẩm!");
            txtTenSanPham.requestFocus();
            return false;
        }
        String giaStr = txtGiaSanPham.getText().trim();
        if (giaStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập đơn giá!");
            txtGiaSanPham.requestFocus();
            return false;
        }
        try {
            new BigDecimal(giaStr);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Đơn giá phải là số hợp lệ (ví dụ 15000 hoặc 15000.00)!");
            txtGiaSanPham.requestFocus();
            return false;
        }
        return true;
    }

    private void themSanPham() {
        if (!validateForm()) return;
        try {
            SanPham sp = new SanPham();
            sp.setTenSanPham(txtTenSanPham.getText().trim());
            sp.setDonGia(new BigDecimal(txtGiaSanPham.getText().trim()));
            sp.setMoTa(txtMoTa.getText().trim());

            int newId = dao.insert(sp);
            if (newId > 0) {
                JOptionPane.showMessageDialog(this, "Thêm sản phẩm thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Thêm thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi thêm sản phẩm!");
        }
    }

    private void suaSanPham() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần sửa!"); return; }
        if (!validateForm()) return;

        int id = Integer.parseInt(tableModel.getValueAt(r,0).toString());
        try {
            SanPham sp = new SanPham();
            sp.setMaSanPham(id);
            sp.setTenSanPham(txtTenSanPham.getText().trim());
            sp.setDonGia(new BigDecimal(txtGiaSanPham.getText().trim()));
            sp.setMoTa(txtMoTa.getText().trim());

            if (dao.update(sp)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật!");
        }
    }

    private void xoaSanPham() {
        int r = table.getSelectedRow();
        if (r < 0) { JOptionPane.showMessageDialog(this, "Chọn sản phẩm cần xóa!"); return; }
        int id = Integer.parseInt(tableModel.getValueAt(r,0).toString());

        if (JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa sản phẩm?", "Xác nhận", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION) {
            if (dao.delete(id)) {
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, "Xóa thất bại!");
            }
        }
    }

    private void timKiemSanPham() {
        String key = txtTimKiem.getText().trim();
        if (key.isEmpty()) {
            loadData();
            return;
        }
        List<SanPham> result = dao.search(key);
        tableModel.setRowCount(0);
        for (SanPham sp : result) {
            tableModel.addRow(new Object[] {
                    sp.getMaSanPham(),
                    sp.getTenSanPham(),
                    sp.getDonGia() != null ? sp.getDonGia().toString() : "0",
                    sp.getMoTa()
            });
        }
    }

    private void clearForm() {
        txtTenSanPham.setText("");
        txtGiaSanPham.setText("");
        txtMoTa.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }
}
