package view;

import dao.PhimDAO;
import dao.SuatChieuDAO;
import model.SuatChieu;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PanelSuatChieu extends JPanel {
    // màu chủ đạo
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;

    // dao và data
    private SuatChieuDAO dao = new SuatChieuDAO();
    private PhimDAO phimDAO = new PhimDAO();
    
    // các thành phần giao diện
    private DefaultTableModel model;
    private JTable table;
    private JTextField txtMa, txtGiaVe, txtSearch;
    private JComboBox<String> cbPhim, cbPhong, cbGio, cbTieuChi;
    private com.toedter.calendar.JDateChooser dcNgay;
    
    // lưu mã phim/phòng theo text
    private Map<String, Integer> mapPhim = new HashMap<>();
    private Map<String, Integer> mapPhong = new HashMap<>();
    
    // ds giờ chiếu cố định
    private final String[] GIO_CHIEU = {"09:45", "12:00", "14:20", "16:40", "19:00", "21:20", "23:30"};
    
    // constructor
    public PanelSuatChieu() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(TRANG);

        taoForm(); // form nhập sc
        taoBang(); // bảng ds sc
        taoDuoi(); // tìm kiếm + nút chức năng
        loadComboData(); // load phim + phòng vào combo box
        loadData(); 
    }
    
    // tạo form nhập thông tin sc
    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN SUẤT CHIẾU", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = tf(15); txtMa.setEditable(false); txtMa.setBackground(new Color(245, 245, 245));
        cbPhim = cb(); cbPhong = cb(); dcNgay = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/####", '_');
        cbGio = new JComboBox<>(GIO_CHIEU); txtGiaVe = tf(15);

        int y = 0;
        addRow(p, gbc, y++, "Mã suất chiếu:", txtMa);
        addRow(p, gbc, y++, "Phim:", cbPhim);
        addRow(p, gbc, y++, "Phòng chiếu:", cbPhong);
        addRow(p, gbc, y++, "Ngày chiếu:", dcNgay);
        addRow(p, gbc, y++, "Giờ chiếu:", cbGio);
        addRow(p, gbc, y++, "Giá vé cơ bản:", txtGiaVe);

        add(p, BorderLayout.NORTH);
    }
    
    // tạo bảng danh sách sc
    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH SUẤT CHIẾU", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(new String[]{"Mã suất", "Phim", "Phòng", "Ngày giờ chiếu", "Giá vé"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO); header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setHeaderRenderer(header);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadFormTuBang(table.getSelectedRow());
            }
        });

        p.add(new JScrollPane(table));
        add(p, BorderLayout.CENTER);
    }
    
    // panel dưới: tìm kiếm + nút chức năng
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
        cbTieuChi = new JComboBox<>(new String[]{"Mã suất chiếu", "Phim", "Phòng"});
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

        // Nút chức năng
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        right.setBackground(TRANG);
        right.add(nutDo("Thêm mới", e -> them()));
        right.add(nutDo("Cập nhật", e -> sua()));
        right.add(nutDo("Xóa", e -> xoa()));
        right.add(nutDo("Xóa rỗng", e -> clearForm())); // ĐÃ CÓ HÀM NÀY

        duoi.add(left, BorderLayout.WEST);
        duoi.add(right, BorderLayout.EAST);
        add(duoi, BorderLayout.SOUTH);
    }

    private void loadComboData() {
        cbPhim.removeAllItems(); mapPhim.clear(); cbPhim.addItem("--- Chọn phim ---");
        List<model.Phim> ds = phimDAO.selectAll();
        for (model.Phim p : ds) {
            String item = p.getMaPhim() + " - " + p.getTenPhim();
            cbPhim.addItem(item); mapPhim.put(item, p.getMaPhim());
        }
        cbPhong.removeAllItems();
        for (int i = 1; i <= 3; i++) {
            String item = i + " - Phòng " + i;
            cbPhong.addItem(item); mapPhong.put(item, i);
        }
    }
    
    // load data lên bảng
    private void loadData() {
        model.setRowCount(0);
        List<SuatChieu> list = dao.selectAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (SuatChieu sc : list) {
            model.addRow(new Object[]{
                sc.getMaSuatChieu(),
                sc.getTenPhim(),
                sc.getTenPhongChieu(),
                sdf.format(sc.getNgayGioChieu()),
                String.format("%,.0f đ", sc.getGiaVeCoBan())
            });
        }
    }
    
    // tknc
    private void timKiemNangCao(String keyword, int tieuChi) {
        model.setRowCount(0);
        if (keyword.isEmpty()) {
            loadData();
            return;
        }
        List<SuatChieu> ds = dao.search(keyword, tieuChi);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        for (SuatChieu sc : ds) {
            model.addRow(new Object[]{
                sc.getMaSuatChieu(),
                sc.getTenPhim(),
                sc.getTenPhongChieu(),
                sdf.format(sc.getNgayGioChieu()),
                String.format("%,.0f đ", sc.getGiaVeCoBan())
            });
        }
    }
    
    // điền data lên form khi click
    private void loadFormTuBang(int row) {
        int ma = (int) model.getValueAt(row, 0);
        SuatChieu sc = dao.selectById(ma);
        if (sc != null) {
            txtMa.setText(String.valueOf(sc.getMaSuatChieu()));
            cbPhim.setSelectedItem(mapPhim.entrySet().stream().filter(e -> e.getValue() == sc.getMaPhim()).findFirst().get().getKey());
            cbPhong.setSelectedItem(mapPhong.entrySet().stream().filter(e -> e.getValue() == sc.getMaPhongChieu()).findFirst().get().getKey());
            dcNgay.setDate(sc.getNgayGioChieu());
            cbGio.setSelectedItem(new SimpleDateFormat("HH:mm").format(sc.getNgayGioChieu()));
            txtGiaVe.setText(sc.getGiaVeCoBan().toString());
        }
    }
    
    // các hàm xử lý dữ liệu
    private void them() {
        if (validateForm()) {
            if (dao.insert(getFormData())) {
                thongBao("Thêm suất chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadData();
                clearForm();
            } else {
                thongBao("Không thể thêm! Trùng suất chiếu trong phòng.", "Lỗi", JOptionPane.ERROR_MESSAGE, false);
            }
        }
    }
   
    private void sua() {
        if (txtMa.getText().isEmpty()) {
            thongBao("Vui lòng chọn suất chiếu cần sửa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        if (validateForm()) {
            SuatChieu sc = getFormData();
            sc.setMaSuatChieu(Integer.parseInt(txtMa.getText()));
            if (dao.update(sc)) {
                thongBao("Cập nhật suất chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadData();
                clearForm();
            } else {
                thongBao("Không thể cập nhật! Trùng suất chiếu trong phòng.", "Lỗi", JOptionPane.ERROR_MESSAGE, false);
            }
        }
    }
    
    private void xoa() {
        if (txtMa.getText().isEmpty()) {
            thongBao("Vui lòng chọn suất chiếu cần xóa!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }

        int maSuat = Integer.parseInt(txtMa.getText());
        int confirm = thongBao("Bạn có chắc muốn xóa suất chiếu này?", "Xác nhận xóa", JOptionPane.QUESTION_MESSAGE, true);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(maSuat)) {
                thongBao("Xóa suất chiếu thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadData();
                clearForm();
            } else {
                thongBao("Xóa thất bại! Đã có khách đặt vé!", "Lỗi", JOptionPane.ERROR_MESSAGE, false);
            }
        }
    }

    private void clearForm() { 
        txtMa.setText(""); cbPhim.setSelectedIndex(0); cbPhong.setSelectedIndex(0);
        dcNgay.setDate(null); cbGio.setSelectedIndex(0); txtGiaVe.setText("");
        table.clearSelection();
    }
    
    // lấy dữ liệu từ form
    private SuatChieu getFormData() {
        SuatChieu sc = new SuatChieu();
        sc.setMaPhim(mapPhim.get(cbPhim.getSelectedItem()));
        sc.setMaPhongChieu(mapPhong.get(cbPhong.getSelectedItem()));
        Calendar c = Calendar.getInstance(); c.setTime(dcNgay.getDate());
        String[] t = ((String) cbGio.getSelectedItem()).split(":");
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(t[0])); c.set(Calendar.MINUTE, Integer.parseInt(t[1]));
        sc.setNgayGioChieu(c.getTime());
        sc.setGiaVeCoBan(new BigDecimal(txtGiaVe.getText().trim()));
        return sc;
    }
    
    // kiếm tra dữ liệu nhập 
    private boolean validateForm() {
        if (cbPhim.getSelectedIndex() < 0 || cbPhong.getSelectedIndex() < 0 || dcNgay.getDate() == null || cbGio.getSelectedIndex() < 0 || txtGiaVe.getText().trim().isEmpty()) {
            msg("Vui lòng điền đầy đủ thông tin!"); 
            return false;
        }
      
        try { new BigDecimal(txtGiaVe.getText().trim()); }
        catch (Exception e) { msg("Giá vé không hợp lệ!"); return false; }
        return true;
    }

    // các hàm hỗ trợ giao diện
    private void msg(String s) {
        thongBao(s, "Thông báo", JOptionPane.INFORMATION_MESSAGE, false);
    }

    private JTextField tf(int cols) { JTextField t = new JTextField(cols); t.setFont(new Font("Segoe UI", Font.PLAIN, 14)); return t; }
    private JComboBox<String> cb() { return new JComboBox<>(); }
    private void addRow(JPanel p, GridBagConstraints g, int y, String label, JComponent c) {
        g.gridx = 0; g.gridy = y; g.weightx = 0.3; p.add(new JLabel(label), g);
        g.gridx = 1; g.weightx = 0.7; p.add(c, g);
    }

    private JButton nutDo(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(MAU_DO); b.setForeground(TRANG); b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setPreferredSize(new Dimension(120, 40)); b.setFocusPainted(false); b.setOpaque(true);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addActionListener(a);
        return b;
    }

    private JButton nutDoXam(String text, java.awt.event.ActionListener a) {
        JButton b = new JButton(text);
        b.setBackground(new Color(108, 117, 125)); b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
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
        JButton btnCo    = taoNutDialog("Có",     90, 30);
        JButton btnKhong = taoNutDialog("Không",  90, 30);
        JButton btnOK    = taoNutDialog("OK",    110, 30);

        JOptionPane optionPane;
        if (!coYesNo) {
            optionPane = new JOptionPane(msg, messageType, JOptionPane.DEFAULT_OPTION, null,
                                        new Object[]{btnOK}, btnOK);
        } else {
            optionPane = new JOptionPane(msg, messageType, JOptionPane.YES_NO_OPTION, null,
                                        new Object[]{btnCo, btnKhong}, btnCo);
        }

        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setResizable(false);

        if (!coYesNo) {
            btnOK.addActionListener(e -> dialog.dispose());
        } else {
            btnCo.addActionListener(e -> {
                optionPane.setValue(JOptionPane.YES_OPTION);
                dialog.dispose();
            });
            btnKhong.addActionListener(e -> {
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