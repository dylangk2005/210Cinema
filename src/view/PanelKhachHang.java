package view;

import dao.KhachHangDAO;
import model.KhachHang;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PanelKhachHang extends JPanel implements Refresh {

    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;

    private KhachHangDAO dao = new KhachHangDAO();
    private DefaultTableModel model;
    private JTable table;

    // Các field nhập liệu
    private JTextField txtMa, txtHoTen, txtSDT, txtEmail;
    private JComboBox<String> cbGioiTinh;
    private com.toedter.calendar.JDateChooser dcNgaySinh;
    // Chỉ hiển thị, không cho sửa
    private JTextField txtHangTV, txtDiem, txtNgayDK;

    private JTextField txtSearch;
    private JComboBox<String> cbTieuChi;

    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

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
    public void refreshData() {
        loadData();
        clearForm();
    }

    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN KHÁCH HÀNG", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = tf(8);
        txtMa.setEditable(false);
        txtMa.setBackground(new Color(245, 245, 245));

        txtHoTen = tf(30);
        dcNgaySinh = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        cbGioiTinh = new JComboBox<>(new String[]{"Nam", "Nữ", "Khác"});
        txtSDT = tf(15);
        txtEmail = tf(30);

        // Các field chỉ hiển thị (không cho sửa)
        txtHangTV = tf(12); txtHangTV.setEditable(false); txtHangTV.setBackground(new Color(240, 240, 240));
        txtDiem = tf(10);   txtDiem.setEditable(false);   txtDiem.setBackground(new Color(240, 240, 240));
        txtNgayDK = tf(15); txtNgayDK.setEditable(false); txtNgayDK.setBackground(new Color(240, 240, 240));

        int y = 0;
        addRow(p, gbc, y++, "Mã khách hàng:", txtMa);
        addRow(p, gbc, y++, "Họ và tên:", txtHoTen);
        addRow(p, gbc, y++, "Ngày sinh:", dcNgaySinh);
        addRow(p, gbc, y++, "Giới tính:", cbGioiTinh);
        addRow(p, gbc, y++, "Số điện thoại:", txtSDT);
        addRow(p, gbc, y++, "Email:", txtEmail);
        addRow(p, gbc, y++, "Hạng thành viên:", txtHangTV);
        addRow(p, gbc, y++, "Điểm tích lũy:", txtDiem);
        addRow(p, gbc, y++, "Ngày đăng ký:", txtNgayDK);

        add(p, BorderLayout.NORTH);
    }

    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH KHÁCH HÀNG", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(new String[]{
            "Mã KH", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Email", "Hạng TV", "Điểm", "Ngày ĐK"
        }, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Header đỏ
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO);
        header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        table.getTableHeader().setDefaultRenderer(header);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillForm(table.getSelectedRow());
            }
        });

        p.add(new JScrollPane(table));
        add(p, BorderLayout.CENTER);
    }

    private void taoDuoi() {
        JPanel duoi = new JPanel(new BorderLayout(20, 0));
        duoi.setBackground(TRANG);
        duoi.setBorder(new EmptyBorder(10, 0, 10, 0));

        // Tìm kiếm
        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(TRANG);

        JPanel tieuChiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tieuChiPanel.setBackground(TRANG);
        tieuChiPanel.add(new JLabel("Tìm theo:"));
        cbTieuChi = new JComboBox<>(new String[]{"Mã KH", "Họ tên", "Số điện thoại"});
        cbTieuChi.setPreferredSize(new Dimension(150, 32));
        tieuChiPanel.add(cbTieuChi);

        JPanel oTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        oTim.setBackground(TRANG);
        txtSearch = new JTextField(28);
        txtSearch.setPreferredSize(new Dimension(300, 32));
        JButton btnRefresh = nutDoXam("Làm mới", e -> {
            txtSearch.setText("");
            cbTieuChi.setSelectedIndex(0);
            loadData();
        });
        oTim.add(txtSearch);
        oTim.add(btnRefresh);

        left.add(tieuChiPanel);
        left.add(oTim);

        txtSearch.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) { timKiem(); }
            private void timKiem() {
                String key = txtSearch.getText().trim();
                int type = cbTieuChi.getSelectedIndex();
                timKiemNangCao(key, type);
            }
        });

        // Nút chức năng
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        right.setBackground(TRANG);
        right.add(nutDo("Cập nhật", e -> sua()));
        right.add(nutDo("Xóa", e -> xoa()));
        right.add(nutDo("Xóa rỗng", e -> clearForm()));

        duoi.add(left, BorderLayout.WEST);
        duoi.add(right, BorderLayout.EAST);
        add(duoi, BorderLayout.SOUTH);
    }

    private void loadData() {
        model.setRowCount(0);
        List<KhachHang> list = dao.getAll();
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

    private void fillForm(int row) {
        int ma = (int) model.getValueAt(row, 0);
        KhachHang kh = dao.getById(ma);
        if (kh != null) {
            txtMa.setText(String.valueOf(ma));
            txtHoTen.setText(kh.getHoTenKhachHang());
            dcNgaySinh.setDate(kh.getNgaySinh());
            cbGioiTinh.setSelectedItem(kh.getGioiTinh());
            txtSDT.setText(kh.getSoDienThoai());
            txtEmail.setText(kh.getEmail() != null ? kh.getEmail() : "");
            txtHangTV.setText(kh.getHangThanhVien());
            txtDiem.setText(String.valueOf(kh.getDiemTichLuy()));
            txtNgayDK.setText(kh.getNgayDangKy() != null ? sdf.format(kh.getNgayDangKy()) : "");
        }
    }

    private void sua() {
        if (txtMa.getText().isEmpty()) {
            msg("Vui lòng chọn khách hàng cần sửa!");
            return;
        }
        if (validateForm()) {
            KhachHang kh = getFormData();
            kh.setMaKhachHang(Integer.parseInt(txtMa.getText()));
            kh.setHangThanhVien(txtHangTV.getText());
            kh.setDiemTichLuy(Integer.parseInt(txtDiem.getText()));
            
           try {
                String ngayText = txtNgayDK.getText().trim();
                // String -> java.util.Date
                java.util.Date utilDate = sdf.parse(ngayText);
                // java.util.Date -> java.sql.Date
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());

                kh.setNgayDangKy(sqlDate);
            } catch (Exception e) {
                msg("Ngày đăng ký không hợp lệ! Định dạng: dd/MM/yyyy");
                return;
            }
           
            KhachHang temp = new KhachHangDAO().getKhachHangBySDT(kh.getSoDienThoai(), kh.getMaKhachHang());
            if (temp != null){
                msg("Số điện thoại bị trùng. Không thể cập nhật!");
                clearForm();
                return;
            }
            if (dao.update(kh)) {
                msg("Cập nhật khách hàng thành công!");
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
        int confirm = thongBao("Xóa khách hàng này?\nDữ liệu liên quan sẽ bị ảnh hưởng!", 
                "Xác nhận xóa", JOptionPane.QUESTION_MESSAGE, true);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(Integer.parseInt(txtMa.getText()))) {
                msg("Xóa thành công!");
                loadData();
                clearForm();
            } else {
                msg("Xóa thất bại! Có thể khách đã mua vé.");
            }
        }
    }

    private void clearForm() {
        txtMa.setText("");
        txtHoTen.setText("");
        dcNgaySinh.setDate(null);
        cbGioiTinh.setSelectedIndex(0);
        txtSDT.setText("");
        txtEmail.setText("");
        txtHangTV.setText("");
        txtDiem.setText("");
        txtNgayDK.setText("");
        table.clearSelection();
    }

    private KhachHang getFormData() {
        KhachHang kh = new KhachHang();
        kh.setHoTenKhachHang(txtHoTen.getText().trim());
        kh.setNgaySinh(dcNgaySinh.getDate());
        kh.setGioiTinh((String) cbGioiTinh.getSelectedItem());
        kh.setSoDienThoai(txtSDT.getText().trim());
        kh.setEmail(txtEmail.getText().trim().isEmpty() ? null : txtEmail.getText().trim());
        // Điểm và hạng sẽ được trigger tự động cập nhật → không cần set ở đây
        return kh;
    }

    // ================== KIỂM TRA DỮ LIỆU SIÊU CHẶT CHẼ (NHƯ CÁC PANEL TRƯỚC) ==================
    private boolean validateForm() {
        List<String> errors = new ArrayList<>();

        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        String email = txtEmail.getText().trim();

       if (hoTen.isEmpty()) {
            errors.add("• Tên khách hàng không được để trống");
        } else if (!hoTen.matches("^[A-Za-zÀ-ỿà-ỹ\\s'-]+$")) {
            errors.add("• Tên khách hàng không được chứa số hoặc ký tự đặc biệt");
        }

         if (sdt.isEmpty()) {
            errors.add("• Số điện thoại không được để trống");
        } else if (!sdt.matches("^0\\d{9,10}$")) {  
            errors.add("• Số điện thoại phải bắt đầu bằng 0 và chỉ chứa 10-11 chữ số");
        }

        if (!email.isEmpty() && !email.matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errors.add("• Email không hợp lệ");
        }

        if (dcNgaySinh == null) {
            errors.add("• Vui lòng chọn ngày sinh");
        } 

        if (!errors.isEmpty()) {
            StringBuilder msg = new StringBuilder("<html><b>Vui lòng sửa các lỗi sau:</b><br><br>");
            for (String err : errors) {
                msg.append("<font color=black> ").append(err).append("</font><br>");
            }
            msg.append("</html>");
            thongBao(msg.toString(), "Dữ liệu không hợp lệ", JOptionPane.ERROR_MESSAGE, false);
            return false;
        }
        return true;
    }

    // ================== HÀM HỖ TRỢ (giống các panel khác) ==================
    private void msg(String s) {
        thongBao(s, "Thông báo", JOptionPane.INFORMATION_MESSAGE, false);
    }

    private JTextField tf(int cols) {
        JTextField t = new JTextField(cols);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        return t;
    }

    private void addRow(JPanel p, GridBagConstraints g, int y, String label, JComponent c) {
        g.gridx = 0; g.gridy = y; g.weightx = 0.3;
        p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 0.7;
        p.add(c, g);
    }

    private JButton nutDo(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(MAU_DO);
        b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(120, 40));
        b.setFocusPainted(false);
        b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(220, 0, 0)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(MAU_DO); }
        });
        b.addActionListener(a);
        return b;
    }

    private JButton nutDoXam(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(new Color(108, 117, 125));
        b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(100, 32));
        b.setFocusPainted(false);
        b.setOpaque(true);
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
                btn.setBackground(new Color(220, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(180, 0, 0));
            }
        });
        return btn;
    }
}