package view;

import com.toedter.calendar.JDateChooser;
import dao.KhachHangDAO;
import model.KhachHang;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.List;

public class PanelKhachHang extends JPanel implements Refresh{

    // ================== MÀU SẮC CHỦ ĐẠO ==================
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;

    // ================== DAO & DỮ LIỆU ==================
    private KhachHangDAO dao = new KhachHangDAO();
    private DefaultTableModel model;
    private JTable table;

    // Các ô nhập liệu
    private JTextField txtMa, txtHoTen, txtGioiTinh, txtSDT, txtEmail, txtHangTV, txtDiem;
    private JDateChooser dcNgaySinh, dcNgayDK;
    private JTextField txtSearch;
    private JComboBox<String> cbTieuChi;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // ================== CONSTRUCTOR ==================
    public PanelKhachHang() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(TRANG);

        taoForm();
        taoBang();
        taoDuoi();
        loadData();
    }
    
    @Override
    public void refreshData(){
        loadData();
    }
    // ================== 1. TẠO FORM NHẬP THÔNG TIN KHÁCH HÀNG ==================
    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN KHÁCH HÀNG", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Khởi tạo các field
        txtMa = tf(8); txtMa.setEditable(false); txtMa.setBackground(new Color(245,245,245));
        txtHoTen = tf(25); 
        dcNgaySinh = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        txtGioiTinh = tf(10);
        txtSDT = tf(15); 
        txtEmail = tf(25); 
        txtHangTV = tf(12); 
        txtDiem = tf(10); 
        dcNgayDK = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/####", '_'); 

        int y = 0;
        addRow(p, gbc, y++, "Mã khách hàng:", txtMa);
        addRow(p, gbc, y++, "Họ tên:", txtHoTen);
        addRow(p, gbc, y++, "Ngày sinh:", dcNgaySinh);
        addRow(p, gbc, y++, "Giới tính:", txtGioiTinh);
        addRow(p, gbc, y++, "Số điện thoại:", txtSDT);
        addRow(p, gbc, y++, "Email:", txtEmail);
        addRow(p, gbc, y++, "Hạng thành viên:", txtHangTV);
        addRow(p, gbc, y++, "Điểm tích lũy:", txtDiem);
        addRow(p, gbc, y++, "Ngày đăng ký:", dcNgayDK);

        add(p, BorderLayout.NORTH);
    }

    // ================== 2. TẠO BẢNG DANH SÁCH KHÁCH HÀNG ==================
    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH KHÁCH HÀNG", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(new String[]{
            "Mã khách hàng", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Hạng TV", "Điểm", "Ngày ĐK"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Header đỏ đẹp
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO); header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(header);
        }

        // Click vào dòng → điền form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillForm(table.getSelectedRow());
            }
        });

        p.add(new JScrollPane(table));
        add(p, BorderLayout.CENTER);
    }

    // ================== 3. PHẦN DƯỚI: TÌM KIẾM + NÚT CHỨC NĂNG ==================
    private void taoDuoi() {
        JPanel duoi = new JPanel(new BorderLayout(20, 0));
        duoi.setBackground(TRANG);
        duoi.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Bên trái: Tìm kiếm
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(TRANG);

        JPanel tieuChiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tieuChiPanel.setBackground(TRANG);
        tieuChiPanel.add(new JLabel("Tìm theo:"));
        cbTieuChi = new JComboBox<>(new String[]{"Mã khách hàng", "Họ tên", "Số điện thoại"});
        cbTieuChi.setPreferredSize(new Dimension(150, 32));
        tieuChiPanel.add(cbTieuChi);

        JPanel oTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        oTim.setBackground(TRANG);
        txtSearch = new JTextField(28);
        txtSearch.setPreferredSize(new Dimension(280, 32));
        JButton btnRefresh = nutDoXam("Làm mới", e -> {
            txtSearch.setText("");
            cbTieuChi.setSelectedIndex(0);
            loadData();
        });
        oTim.add(txtSearch);
        oTim.add(btnRefresh);
        left.add(tieuChiPanel);
        left.add(oTim);

        // Tìm kiếm tức thì khi gõ
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timKiem(); }
            public void removeUpdate(DocumentEvent e) { timKiem(); }
            public void changedUpdate(DocumentEvent e) { timKiem(); }
            private void timKiem() {
                String key = txtSearch.getText().trim();
                int type = cbTieuChi.getSelectedIndex();
                timKiemNangCao(key, type);
            }
        });

        // Bên phải: Các nút chức năng
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        right.setBackground(TRANG);
        right.add(nutDo("Cập nhật", e -> sua()));
        right.add(nutDo("Xóa", e -> xoa()));
        right.add(nutDo("Xóa rỗng", e -> clearForm()));

        duoi.add(left, BorderLayout.WEST);
        duoi.add(right, BorderLayout.EAST);
        add(duoi, BorderLayout.SOUTH);
    }

    // ================== LOAD DỮ LIỆU LÊN BẢNG ==================
    private void loadData() {
        model.setRowCount(0);
        List<KhachHang> list = dao.getAll();
        if (list == null) return;
        for (KhachHang kh : list) {
            model.addRow(new Object[]{
                kh.getMaKhachHang(),
                kh.getHoTenKhachHang(),
                kh.getNgaySinh() != null ? sdf.format(kh.getNgaySinh()) : "",
                kh.getGioiTinh(),
                kh.getSoDienThoai(),
                kh.getEmail(),
                kh.getHangThanhVien(),
                kh.getDiemTichLuy(),
                kh.getNgayDangKy() != null ? sdf.format(kh.getNgayDangKy()) : ""
            });
        }
    }

    // ================== TÌM KIẾM NÂNG CAO ==================
    private void timKiemNangCao(String keyword, int tieuChi) {
        model.setRowCount(0);
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        List<KhachHang> ds = dao.search(keyword, tieuChi);
        for (KhachHang kh : ds) {
            model.addRow(new Object[]{
                kh.getMaKhachHang(),
                kh.getHoTenKhachHang(),
                kh.getNgaySinh() != null ? sdf.format(kh.getNgaySinh()) : "",
                kh.getGioiTinh(),
                kh.getSoDienThoai(),
                kh.getEmail(),
                kh.getHangThanhVien(),
                kh.getDiemTichLuy(),
                kh.getNgayDangKy() != null ? sdf.format(kh.getNgayDangKy()) : ""
            });
        }
    }

    // ================== ĐIỀN FORM KHI CLICK BẢNG ==================
    private void fillForm(int row) {
        KhachHang kh = dao.getById((int) model.getValueAt(row, 0));
        if (kh != null) {
            txtMa.setText(String.valueOf(kh.getMaKhachHang()));
            txtHoTen.setText(kh.getHoTenKhachHang());
            dcNgaySinh.setDate(kh.getNgaySinh());
            txtGioiTinh.setText(kh.getGioiTinh());
            txtSDT.setText(kh.getSoDienThoai());
            txtEmail.setText(kh.getEmail());
            txtHangTV.setText(kh.getHangThanhVien());
            txtDiem.setText(String.valueOf(kh.getDiemTichLuy()));
            dcNgayDK.setDate(kh.getNgayDangKy());
        }
    }

    // ================== CÁC CHỨC NĂNG ==================
    private void sua() {
        if (txtMa.getText().isEmpty()) {
            msg("Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        if (validateForm()) {
            KhachHang kh = getFormData();
            kh.setMaKhachHang(Integer.parseInt(txtMa.getText()));
            if (dao.update(kh)) {
                msg("Cập nhật thành công!");
                loadData();
                clearForm();
            } else {
                msg("Cập nhật thất bại!");
            }
        }
    }

    private void xoa() {
        if (txtMa.getText().isEmpty()) {
            msg("Vui lòng chọn khách hàng cần xóa!");
            return;
        }
        int confirm = thongBao("Bạn có chắc chắn muốn xóa khách hàng này?\n",
                "Xác nhận xóa", JOptionPane.QUESTION_MESSAGE, true);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(Integer.parseInt(txtMa.getText()))) {
                msg("Xóa thành công!");
                loadData();
                clearForm();
            } else {
                msg("Xóa thất bại! Khách hàng đang có đơn hàng.");
            }
        }
    }

    private void clearForm() {
        txtMa.setText(""); 
        txtHoTen.setText(""); 
        dcNgaySinh.setDate(null);
        txtGioiTinh.setText("");
        txtSDT.setText(""); 
        txtEmail.setText(""); 
        txtHangTV.setText(""); 
        txtDiem.setText(""); 
        dcNgayDK.setDate(null);
        table.clearSelection();
    }

    private KhachHang getFormData() {
        KhachHang kh = new KhachHang();
        kh.setHoTenKhachHang(txtHoTen.getText().trim());
        kh.setNgaySinh(dcNgaySinh.getDate());
        kh.setGioiTinh(txtGioiTinh.getText().trim());
        kh.setSoDienThoai(txtSDT.getText().trim());
        kh.setEmail(txtEmail.getText().trim());
        kh.setHangThanhVien(txtHangTV.getText().trim());
        try { kh.setDiemTichLuy(Integer.parseInt(txtDiem.getText().trim())); }
        catch (Exception e) { kh.setDiemTichLuy(0); }
        kh.setNgayDangKy(dcNgayDK.getDate());
        return kh;
    }

    private boolean validateForm() {
        if (txtHoTen.getText().trim().isEmpty() || txtSDT.getText().trim().isEmpty()) {
            msg("Vui lòng nhập họ tên và số điện thoại!");
            return false;
        }
        if (!txtSDT.getText().matches("\\d{10,11}")) {
            msg("Số điện thoại không hợp lệ!");
            return false;
        }
        return true;
    }

    // ================== CÁC HÀM HỖ TRỢ ==================
    private void msg(String s) {
        thongBao(s, "Thông báo", JOptionPane.INFORMATION_MESSAGE, false);
    }

    private JTextField tf(int cols) {
        JTextField t = new JTextField(cols);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return t;
    }

    private void addRow(JPanel p, GridBagConstraints g, int y, String label, JComponent c) {
        g.gridx = 0; g.gridy = y; g.weightx = 0.3; p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 0.7; p.add(c, g);
    }

    private JButton nutDo(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(MAU_DO); b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(120, 40));
        b.setFocusPainted(false); b.setOpaque(true);
        // hand cursor + hover
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(220, 0, 0)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(MAU_DO); }
        });
         b.addActionListener(a);
        return b;
    }

    private JButton nutDoXam(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(new Color(108, 117, 125)); b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(100, 32));
        b.setFocusPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(130, 140, 150)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(new Color(108, 117, 125)); }
        });
        b.addActionListener(a);
        return b;
    }

    
    private int thongBao(String msg, String title, int messageType, boolean coYesNo) {
        JButton btnHuy = taoNutDialog("Hủy", 90, 30);
        JButton btnOK    = taoNutDialog("OK", 90, 30);

        JOptionPane optionPane;
        if (!coYesNo) {
            optionPane = new JOptionPane(msg, messageType, JOptionPane.DEFAULT_OPTION, null,
                                        new Object[]{btnOK}, btnOK);
        } else {
            optionPane = new JOptionPane(msg, messageType, JOptionPane.YES_NO_OPTION, null,
                                        new Object[]{btnHuy, btnOK}, btnOK);
        }

        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setResizable(false);

        if (!coYesNo) {
            btnOK.addActionListener(e -> dialog.dispose());
        } else {
            btnOK.addActionListener(e -> {
                optionPane.setValue(JOptionPane.YES_OPTION);
                dialog.dispose();
            });
            btnHuy.addActionListener(e -> {
                optionPane.setValue(JOptionPane.NO_OPTION);
                dialog.dispose();
            });
        }

        dialog.setVisible(true);

        Object value = optionPane.getValue();
        if (value == null) return JOptionPane.CLOSED_OPTION;
        if (value instanceof Integer) return (Integer) value;
        return JOptionPane.CLOSED_OPTION;
    }
    
     // Hàm hỗ trợ tạo nút cho dialog
    private JButton taoNutDialog(String text, int width, int height) {
        JButton btn = new JButton(text);
        btn.setPreferredSize(new Dimension(width, height));
        btn.setMinimumSize(new Dimension(width, height));
        btn.setMaximumSize(new Dimension(width, height));

        btn.setBackground(MAU_DO);     // đỏ đậm
        btn.setForeground(TRANG);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 15));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        // Hover 
        btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(200, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(180, 0, 0));
            }
        });
        return btn;
    }
}