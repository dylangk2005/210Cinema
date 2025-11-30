package view;

import dao.SanPhamDAO;
import model.SanPham;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

public class PanelSanPham extends JPanel implements Refresh {
    
    // ================== MÀU SẮC CHỦ ĐẠO ==================
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;
    
    // ================== DAO & DỮ LIỆU ==================
    private SanPhamDAO dao = new SanPhamDAO();
    private DefaultTableModel model;
    private JTable table;

    private JTextField txtMa, txtTen, txtGia, txtMoTa;
    private JTextField txtSearch;
    private JComboBox<String> cbTieuChi;
    
    // constructor
    public PanelSanPham() {
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
        clearForm();
    }
    
    // ================== 1. TẠO FORM NHẬP THÔNG TIN SP ==================
    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN SẢN PHẨM", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = tf(8); txtMa.setEditable(false); txtMa.setBackground(new Color(245,245,245));
        txtTen = tf(25); 
        txtGia = tf(15);
        txtMoTa = tf(50);
     
        int y = 0;
        addRow(p, gbc, y++, "Mã Sản phẩm:", txtMa);
        addRow(p, gbc, y++, "Tên sản phẩm:", txtTen);
        addRow(p, gbc, y++, "Đơn giá:", txtGia);
        addRow(p, gbc, y++, "Mô tả", txtMoTa);

        add(p, BorderLayout.NORTH);
    }
    
    // ================== 2. TẠO BẢNG DANH SÁCH SP ==================
    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH SẢN PHẨM", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(new String[]{"Mã sản phẩm", "Tên sản phẩm", "Đơn giá", "Mô tả"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(50);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO); header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(header);
        }

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

        JPanel left = new JPanel();
        left.setLayout(new BoxLayout(left, BoxLayout.Y_AXIS));
        left.setBackground(TRANG);

        JPanel tieuChiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tieuChiPanel.setBackground(TRANG);
        tieuChiPanel.add(new JLabel("Tìm theo:"));
        cbTieuChi = new JComboBox<>(new String[]{"Mã sản phẩm", "Tên sản phẩm"});
        cbTieuChi.setPreferredSize(new Dimension(130, 32));
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
        
        // nút chức năng
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
    
    // ================== LOAD DỮ LIỆU LÊN BẢNG ==================
    private void loadData() {
        model.setRowCount(0);
        List<SanPham> list = dao.getAll();
        for (SanPham sp : list) {
            model.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                String.format("%,.0f đ", sp.getDonGia()),
                sp.getMoTa()
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
        List<SanPham> ds = dao.search(keyword, tieuChi);
        for (SanPham sp : ds) {
            model.addRow(new Object[]{
                sp.getMaSanPham(),
                sp.getTenSanPham(),
                String.format("%,.0f đ", sp.getDonGia()),
                sp.getMoTa()
            });
        }
    }
    
    // ================== ĐIỀN FORM KHI CLICK BẢNG ==================
    private void fillForm(int row) {
        SanPham sp = dao.getAll().get(row); // Đơn giản vì đã có trong bảng
        txtMa.setText(String.valueOf(sp.getMaSanPham()));
        txtTen.setText(sp.getTenSanPham());
        txtGia.setText(sp.getDonGia().toString());
        txtMoTa.setText(sp.getMoTa() != null ? sp.getMoTa() : "");
    }
    
    // ================== CÁC CHỨC NĂNG ==================
    private void them() {
        if (validateForm()) {
            String t = txtTen.getText().trim();
            if (new SanPhamDAO().kiemTraTrungSP(t, -1)){
                msg("Trùng tên sản phẩm. Không thể thêm");
                return;
            }
            SanPham sp = getFormData();
            int id = dao.insert(sp);
            if (id > 0) {
                msg("Thêm sản phẩm thành công!");
                loadData();
            } else {
                msg("Thêm thất bại!");
            }
        }
    }

    private void sua() {
        if (txtMa.getText().isEmpty()) { msg("Chọn sản phẩm cần sửa!"); return; }
        if (validateForm()) {
            SanPham sp = getFormData();
            sp.setMaSanPham(Integer.parseInt(txtMa.getText()));
            if (new SanPhamDAO().kiemTraTrungSP(sp.getTenSanPham(), sp.getMaSanPham())){
                msg("Trùng tên sản phẩm. Không thể cập nhật!");
                return;
            }
            if (dao.update(sp)) {
                msg("Cập nhật thành công!");
                loadData();
            } else {
                msg("Cập nhật thất bại!");
            }
        }
    }

    private void xoa() {
        if (txtMa.getText().isEmpty()) { msg("Chọn sản phẩm cần xóa!"); return; }
        int confirm = thongBao("Xóa sản phẩm này?", "Xác nhận", JOptionPane.QUESTION_MESSAGE, true);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(Integer.parseInt(txtMa.getText()))) {
                msg("Xóa thành công!");
                loadData();
            } else {
                msg("Xóa thất bại! Có thể đã có đơn hàng.");
            }
        }
    }

    private void clearForm() {
        txtMa.setText(""); txtTen.setText(""); txtGia.setText(""); txtMoTa.setText("");
        table.clearSelection();
    }

    private SanPham getFormData() {
        SanPham sp = new SanPham();
        sp.setTenSanPham(txtTen.getText().trim());
        sp.setDonGia(new BigDecimal(txtGia.getText().trim().replace(",", "")));
        sp.setMoTa(txtMoTa.getText().trim());
        return sp;
    }

   private boolean validateForm() {
        java.util.List<String> errors = new java.util.ArrayList<>();

        String ten = txtTen.getText().trim();
        String giaStr = txtGia.getText().trim().replace(",", "").replace(".", "");

        // 1. Kiểm tra tên sản phẩm
        if (ten.isEmpty()) {
            errors.add("• Tên sản phẩm không được để trống");
        } else if (!ten.matches("^[A-Za-zÀ-ỿà-ỹ0-9\\s'-,:.]+$")) {
            errors.add("• Tên sản phẩm không hợp lệ!");
        }
        
        // 2. Kiểm tra đơn giá
        if (giaStr.isEmpty()) {
            errors.add("• Vui lòng nhập đơn giá");
        } else {
            try {
                BigDecimal donGia = new BigDecimal(giaStr);
                if (donGia.compareTo(BigDecimal.ZERO) <= 0) {
                    errors.add("• Đơn giá phải lớn hơn 0");
                }
                if (donGia.scale() > 0) { // có phần thập phân
                    errors.add("• Đơn giá không được có phần lẻ (chỉ nhập số nguyên)");
                }
            } catch (Exception e) {
                errors.add("• Đơn giá phải là số hợp lệ (ví dụ: 25000)");
            }
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
    
    // ================== CÁC HÀM HỖ TRỢ ==================
    private void msg(String s) { thongBao(s, "Thông báo", JOptionPane.INFORMATION_MESSAGE, false); }

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
        //hiện bàn tay khi click + hover
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
                btn.setBackground(new Color(220, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btn.setBackground(new Color(180, 0, 0));
            }
        });
        return btn;
    }
}