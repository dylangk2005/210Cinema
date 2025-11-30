package view;

import dao.PhimDAO;
import model.Phim;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class PanelPhim extends JPanel implements Refresh {
    // màu chủ đạo
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;
    
    // dao + các tp giao diện
    private PhimDAO dao = new PhimDAO();

    private DefaultTableModel model;
    private JTable table;
    
    // ô nhập liệu
    private JTextField txtMa, txtTen, txtThoiLuong, txtTheLoai, txtMoTa, txtSearch;
    private JComboBox<String> cbGioiHanTuoi, cbTieuChi;
    private com.toedter.calendar.JDateChooser dcNgayKhoiChieu;

    
    // constructor
    public PanelPhim() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(TRANG);

        taoForm(); // form nhập liệu
        taoBang(); // bảng danh sách phim
        taoDuoi(); // tìm kiếm + chức năng
        loadData(); // load dữ liệu lần dầu
    }
    
    @Override
    public void refreshData(){
        loadData();
    }

    // form nhập liệu
    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN PHIM", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        txtMa = tf(10); txtMa.setEditable(false); txtMa.setBackground(new Color(245, 245, 245));
        txtTen = tf(30); txtThoiLuong = tf(10);
        txtTheLoai = tf(40);           // Thể loại
        txtMoTa = tf(50);              // Mô tả – ĐƯỢC ĐẨY XUỐNG CUỐI
        dcNgayKhoiChieu = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        cbGioiHanTuoi = new JComboBox<>(new String[]{"P", "C13", "C16", "C18"});

        int y = 0;
        addRow(p, gbc, y++, "Mã phim:", txtMa);
        addRow(p, gbc, y++, "Tên phim:", txtTen);
        addRow(p, gbc, y++, "Thời lượng (phút):", txtThoiLuong);
        addRow(p, gbc, y++, "Thể loại:", txtTheLoai);
        addRow(p, gbc, y++, "Giới hạn tuổi:", cbGioiHanTuoi);
        addRow(p, gbc, y++, "Ngày khởi chiếu:", dcNgayKhoiChieu);
        addRow(p, gbc, y++, "Mô tả:", txtMoTa);  // ← MÔ TẢ Ở DÒNG CUỐI CÙNG

        add(p, BorderLayout.NORTH);
    }
    
     // bảng ds phim
    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH PHIM", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(new String[]{
            "Mã phim", "Tên phim", "Thời lượng", "Thể loại", "Giới hạn tuổi", "Ngày khởi chiếu", "Mô tả"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };


        table = new JTable(model);
        table.setRowHeight(60);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tô màu header
        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO); header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(header);
        }
        
        // click vào -> hiện dữ liệu lên form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillForm(table.getSelectedRow());
            }
        });

        p.add(new JScrollPane(table));
        add(p, BorderLayout.CENTER);
    }
    
     // tìm kiếm + nút chức năng
    private void taoDuoi() {
        JPanel duoi = new JPanel(new BorderLayout(20, 0));
        duoi.setBackground(TRANG);
        duoi.setBorder(new EmptyBorder(10, 0, 10, 0));

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(TRANG);

        JPanel tieuChiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tieuChiPanel.setBackground(TRANG);
        tieuChiPanel.add(new JLabel("Tìm theo:"));
        cbTieuChi = new JComboBox<>(new String[]{"Mã phim", "Tên phim", "Thể loại"});
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
        
        // tìm kiếm tức thì khi gõ
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
        
        // các nút chức năng
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        right.setBackground(TRANG);
        right.add(nutDo("Thêm mới", e -> them()));
        right.add(nutDo("Cập nhật", e -> sua()));
        right.add(nutDo("Xóa", e -> xoa()));
        right.add(nutDo("Xóa rỗng", e -> clearForm()));

        duoi.add(left, BorderLayout.WEST);
        duoi.add(right, BorderLayout.EAST);
        add(duoi, BorderLayout.SOUTH);
    }
    
    // load data lên bảng
    private void loadData() {
        model.setRowCount(0);
        List<Phim> list = dao.selectAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Phim p : list) {
           model.addRow(new Object[]{
                p.getMaPhim(),
                p.getTenPhim(),
                p.getThoiLuong(),
                p.getTheLoai(),
                p.getGioiHanTuoi(),
                p.getNgayKhoiChieu() != null ? sdf.format(p.getNgayKhoiChieu()) : "",
                p.getMoTa()
            });
        }
    }
    
    // tknc
    private void timKiemNangCao(String keyword, int tieuChi) {
        model.setRowCount(0);
        if (keyword.isEmpty()) { loadData(); return; }
        List<Phim> ds = dao.search(keyword, tieuChi);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        for (Phim p : ds) {
            model.addRow(new Object[]{
                p.getMaPhim(),
                p.getTenPhim(),
                p.getThoiLuong(),
                p.getTheLoai(),
                p.getMoTa(),
                p.getGioiHanTuoi(),
                p.getNgayKhoiChieu() != null ? sdf.format(p.getNgayKhoiChieu()) : ""
            });
        }
    }
    
    // điền dữ liệu lên form khi click
    private void fillForm(int row) {
        Phim p = dao.selectById((int) model.getValueAt(row, 0));
        if (p != null) {
            txtMa.setText(String.valueOf(p.getMaPhim()));
            txtTen.setText(p.getTenPhim());
            txtThoiLuong.setText(String.valueOf(p.getThoiLuong()));
            txtTheLoai.setText(p.getTheLoai());
            cbGioiHanTuoi.setSelectedItem(p.getGioiHanTuoi());
            dcNgayKhoiChieu.setDate(p.getNgayKhoiChieu());
            txtMoTa.setText(p.getMoTa());
        }
    }
    
    // thêm phim
    private void them(){ 
        if (checkForm()){
            String t = txtTen.getText().trim();
            if (new PhimDAO().kiemTraTrungPhim(t, -1)) {
                msg("Trùng tên Phim. Không thể thêm!");
                return;
            }
            if (dao.insert(getData())){ 
                msg("Thêm phim thành công!"); 
                loadData(); 
                clearForm(); 
            }else msg("Thêm thất bại!"); 
        }
    }
    
    // update phim
    private void sua() { 
        if (txtMa.getText().isEmpty()){ 
            msg("Chọn phim cần cập nhật!"); 
            return; 
        } 
        if (checkForm()){ 
            Phim p = getData(); 
            p.setMaPhim(Integer.parseInt(txtMa.getText()));
            if (new PhimDAO().kiemTraTrungPhim(p.getTenPhim(), p.getMaPhim())){
                msg("Trùng tên phim. Không thể cập nhật");
                return;
            }
            if (dao.update(p)){ 
                msg("Cập nhật thành công!"); 
                loadData(); 
                clearForm(); 
            } else msg("Đã có suất chiếu của phim này! Không thể cập nhật"); } }
    
    // xóa phim (kiếm tra xem có suất chiếu ko)
    private void xoa(){
        if (txtMa.getText().isEmpty()) {
            thongBao("Vui lòng chọn phim cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        
        int maPhim = Integer.parseInt(txtMa.getText());
        int confirm = thongBao("Bạn có chắc chắn muốn xóa phim này?\nPhim đã chọn: " + txtTen.getText(), 
                              "Xác nhận xóa", JOptionPane.QUESTION_MESSAGE, true);

        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(maPhim)) {
                thongBao("Xóa phim thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadData();
                clearForm();
            } else {
                thongBao("Không thể xóa!\nPhim đang được sử dụng trong lịch chiếu hoặc có dữ liệu liên quan.", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE, false);
            }
        }
    }
    
    // Xóa rỗng form
    private void clearForm(){ 
        txtMa.setText(""); 
        txtTen.setText(""); 
        txtThoiLuong.setText(""); 
        txtTheLoai.setText(""); 
        txtMoTa.setText(""); 
        cbGioiHanTuoi.setSelectedIndex(0); 
        dcNgayKhoiChieu.setDate(null); 
        table.clearSelection(); 
    }
    
    // Lấy dữ liệu từ form -> đối tượng phim
    private Phim getData() {
        Phim p = new Phim();
        p.setTenPhim(txtTen.getText().trim());
        p.setThoiLuong(Integer.parseInt(txtThoiLuong.getText().trim()));
        p.setTheLoai(txtTheLoai.getText().trim());
        p.setMoTa(txtMoTa.getText().trim());
        p.setGioiHanTuoi((String) cbGioiHanTuoi.getSelectedItem());
        p.setNgayKhoiChieu(dcNgayKhoiChieu.getDate() != null ? new Date(dcNgayKhoiChieu.getDate().getTime()) : null);
        return p;
    }
    
    // kiểm tra dữ liệu nhập
    private boolean checkForm() {
        java.util.List<String> errors = new java.util.ArrayList<>();

        String tenPhim = txtTen.getText().trim();
        String theLoai = txtTheLoai.getText().trim();
        String thoiLuongStr = txtThoiLuong.getText().trim();

        // 1. Tên phim
        if (tenPhim.isEmpty()) {
            errors.add("• Tên phim không được để trống");
        } else if (!tenPhim.matches("^[A-Za-zÀ-ỿà-ỹ0-9\\s'-:!.]+$")) {
            errors.add("• Tên phim không hợp lệ!");
        }
        
        // 2. Thể loại
        if (theLoai.isEmpty()) {
            errors.add("• Thể loại không được để trống");
        } else if (!theLoai.matches("^[A-Za-zÀ-ỿà-ỹ\\s,]+$")) {
            errors.add("• Thể loại không hợp lệ!");
        }

        // 3. Thời lượng
        if (thoiLuongStr.isEmpty()) {
            errors.add("• Thời lượng không được để trống");
        } else {
            try {
                int thoiLuong = Integer.parseInt(thoiLuongStr);
                if (thoiLuong < 60 || thoiLuong > 300) {
                    errors.add("• Thời lượng phải từ 60 đến 300 phút");
                }
            } catch (NumberFormatException e) {
                errors.add("• Thời lượng phải là số nguyên hợp lệ");
            }
        }

        // 4. Ngày khởi chiếu
        if (dcNgayKhoiChieu.getDate() == null) {
            errors.add("• Vui lòng chọn ngày khởi chiếu");
        }

        // 5. Giới hạn tuổi
        if (cbGioiHanTuoi.getSelectedIndex() == -1) {
            errors.add("• Vui lòng chọn giới hạn tuổi");
        }

        // === HIỂN THỊ TẤT CẢ LỖI CÙNG LÚC ===
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
    
    // ================== CÁC HÀM HỖ TRỢ GIAO DIỆN ==================
    private void msg(String s) {
        thongBao(s, "Thông báo", JOptionPane.INFORMATION_MESSAGE, false);
    }

    private JTextField tf(int cols) { JTextField t = new JTextField(cols); t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); return t; }
    private void addRow(JPanel p, GridBagConstraints g, int y, String label, JComponent c) {
        g.gridx = 0; g.gridy = y; g.weightx = 0.3; p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 0.7; p.add(c, g);
    }

    private JButton nutDo(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(MAU_DO); b.setForeground(TRANG); b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(120, 40)); b.setFocusPainted(false); b.setOpaque(true);
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
        b.setBackground(new Color(108, 117, 125)); b.setForeground(TRANG); b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(100, 32)); b.setFocusPainted(false); b.setOpaque(true);
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