package view;

import dao.KhachHangDAO;
import model.KhachHang;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class PanelKhachHang extends JPanel {

    private JTextField txtHoTen, txtNgaySinh, txtGioiTinh, txtSoDienThoai, txtEmail, txtHangTV, txtDiem, txtNgayDK;
    private JTextField txtTimKiem;

    private JTable table;
    private DefaultTableModel tableModel;

    private JButton btnSua, btnXoa, btnTimKiem, btnXoaRong;

    private KhachHangDAO khachHangDAO;

    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");

    public PanelKhachHang() {
        khachHangDAO = new KhachHangDAO();
        initComponents();
        setupLayout();
        setupEvents();
        loadData();
    }

    private void initComponents() {
        txtHoTen = new JTextField(15);
        txtNgaySinh = new JTextField(10);
        txtGioiTinh = new JTextField(10);
        txtSoDienThoai = new JTextField(12);
        txtEmail = new JTextField(15);
        txtHangTV = new JTextField(10);
        txtDiem = new JTextField(10);
        txtNgayDK = new JTextField(12);
        txtTimKiem = new JTextField(15);

        String[] columns = {
                "Mã KH", "Họ Tên", "Ngày Sinh", "Giới Tính", "SĐT",
                "Email", "Hạng TV", "Điểm", "Ngày ĐK"
        };

        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        table = new JTable(tableModel);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnXoaRong = new JButton("Xóa Rỗng");
        btnTimKiem = new JButton("Tìm Kiếm");
    }

    private JPanel createFormPanel() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBorder(BorderFactory.createTitledBorder("Thông Tin Khách Hàng"));
        p.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5,5,5,5);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        p.add(new JLabel("Họ Tên:"), gbc);
        gbc.gridx = 1; p.add(txtHoTen, gbc);

        gbc.gridx = 2; p.add(new JLabel("Ngày Sinh (yyyy-MM-dd):"), gbc);
        gbc.gridx = 3; p.add(txtNgaySinh, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        p.add(new JLabel("Giới Tính:"), gbc);
        gbc.gridx = 1; p.add(txtGioiTinh, gbc);

        gbc.gridx = 2; p.add(new JLabel("SĐT:"), gbc);
        gbc.gridx = 3; p.add(txtSoDienThoai, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        p.add(new JLabel("Email:"), gbc);
        gbc.gridx = 1; p.add(txtEmail, gbc);

        gbc.gridx = 2; p.add(new JLabel("Hạng TV:"), gbc);
        gbc.gridx = 3; p.add(txtHangTV, gbc);

        gbc.gridx = 0; gbc.gridy = 3;
        p.add(new JLabel("Điểm TL:"), gbc);
        gbc.gridx = 1; p.add(txtDiem, gbc);

        gbc.gridx = 2; p.add(new JLabel("Ngày ĐK:"), gbc);
        gbc.gridx = 3; p.add(txtNgayDK, gbc);

        return p;
    }

    private JPanel createButtonPanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBackground(Color.WHITE);

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT));
        left.setBackground(Color.WHITE);
        left.add(new JLabel("Tìm theo Mã/SDT:"));
        left.add(txtTimKiem);
        left.add(btnTimKiem);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        right.setBackground(Color.WHITE);
        right.add(btnSua);
        right.add(btnXoa);
        right.add(btnXoaRong);

        p.add(left, BorderLayout.WEST);
        p.add(right, BorderLayout.EAST);
        return p;
    }

    private JPanel createTablePanel() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(BorderFactory.createTitledBorder("Danh Sách Khách Hàng"));
        p.setBackground(Color.WHITE);

        JScrollPane sp = new JScrollPane(table);
        sp.setPreferredSize(new Dimension(0, 300));

        p.add(sp, BorderLayout.CENTER);

        return p;
    }

    private void setupLayout() {
        setLayout(new BorderLayout(10,10));
        setBackground(Color.WHITE);

        add(createFormPanel(), BorderLayout.NORTH);
        add(createTablePanel(), BorderLayout.CENTER);
        add(createButtonPanel(), BorderLayout.SOUTH);
    }

    private void setupEvents() {
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) fillForm();
        });

        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnXoaRong.addActionListener(e -> clearForm());
        btnTimKiem.addActionListener(e -> tim());
    }

    private void loadData() {
        tableModel.setRowCount(0);
        List<KhachHang> list = khachHangDAO.getAll();
        if (list == null) return;

        for (KhachHang kh : list) {
            tableModel.addRow(new Object[]{
                    kh.getMaKhachHang(),
                    kh.getHoTenKhachHang(),
                    df.format(kh.getNgaySinh()),
                    kh.getGioiTinh(),
                    kh.getSoDienThoai(),
                    kh.getEmail(),
                    kh.getHangThanhVien(),
                    kh.getDiemTichLuy(),
                    df.format(kh.getNgayDangKy())
            });
        }
    }

    private void fillForm() {
        int row = table.getSelectedRow();
        if (row < 0) return;

        txtHoTen.setText(tableModel.getValueAt(row,1).toString());
        txtNgaySinh.setText(tableModel.getValueAt(row,2).toString());
        txtGioiTinh.setText(tableModel.getValueAt(row,3).toString());
        txtSoDienThoai.setText(tableModel.getValueAt(row,4).toString());
        txtEmail.setText(tableModel.getValueAt(row,5).toString());
        txtHangTV.setText(tableModel.getValueAt(row,6).toString());
        txtDiem.setText(tableModel.getValueAt(row,7).toString());
        txtNgayDK.setText(tableModel.getValueAt(row,8).toString());
    }

    private void sua() {
        int row = table.getSelectedRow();
        if (row < 0){
            JOptionPane.showMessageDialog(this, "Chọn KH cần sửa!");
            return;
        }

        int maKH = Integer.parseInt(tableModel.getValueAt(row,0).toString());

        try {
            KhachHang kh = new KhachHang();
            kh.setMaKhachHang(maKH);
            kh.setHoTenKhachHang(txtHoTen.getText());
            kh.setNgaySinh(df.parse(txtNgaySinh.getText()));
            kh.setGioiTinh(txtGioiTinh.getText());
            kh.setSoDienThoai(txtSoDienThoai.getText());
            kh.setEmail(txtEmail.getText());
            kh.setHangThanhVien(txtHangTV.getText());
            kh.setDiemTichLuy(Integer.parseInt(txtDiem.getText()));
            kh.setNgayDangKy(df.parse(txtNgayDK.getText()));

            if (khachHangDAO.update(kh)) {
                JOptionPane.showMessageDialog(this, "Cập nhật thành công!");
                loadData();
            } else {
                JOptionPane.showMessageDialog(this, "Cập nhật thất bại!");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Sai định dạng ngày!");
        }
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row < 0){
            JOptionPane.showMessageDialog(this, "Chọn KH cần xóa!");
            return;
        }

        int maKH = Integer.parseInt(tableModel.getValueAt(row,0).toString());

        if (JOptionPane.showConfirmDialog(this, "Xóa khách hàng?", "Xác nhận", JOptionPane.YES_NO_OPTION)
                == JOptionPane.YES_OPTION)
        {
            if (khachHangDAO.delete(maKH)){
                JOptionPane.showMessageDialog(this, "Xóa thành công!");
                loadData();
                clearForm();
            }
        }
    }

    private void tim() {
        String key = txtTimKiem.getText().trim();
        if (key.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Nhập mã KH hoặc SĐT!");
            return;
        }

        KhachHang kh = khachHangDAO.search(key);
        if (kh == null){
            JOptionPane.showMessageDialog(this, "Không tìm thấy!");
            return;
        }

        for (int i = 0; i < tableModel.getRowCount(); i++){
            if (Integer.parseInt(tableModel.getValueAt(i,0).toString()) == kh.getMaKhachHang()){
                table.setRowSelectionInterval(i,i);
                table.scrollRectToVisible(table.getCellRect(i,0,true));
                return;
            }
        }
    }

    private void clearForm() {
        txtHoTen.setText("");
        txtNgaySinh.setText("");
        txtGioiTinh.setText("");
        txtSoDienThoai.setText("");
        txtEmail.setText("");
        txtHangTV.setText("");
        txtDiem.setText("");
        txtNgayDK.setText("");
        txtTimKiem.setText("");
        table.clearSelection();
    }
}
