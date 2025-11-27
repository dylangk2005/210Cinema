package view;

import dao.PhongChieuDAO;
import model.PhongChieu;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelPhongChieu extends JPanel implements Refresh {

    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;

    private PhongChieuDAO dao = new PhongChieuDAO();
    private DefaultTableModel model;
    private JTable table;

    private JTextField txtMa, txtTen;
    private JComboBox<String> cbSoGhe, cbTrangThai, cbManHinh, cbAmThanh;
    private JTextField txtSearch;
    private JComboBox<String> cbTieuChi;

    public PanelPhongChieu() {
        setLayout(new BorderLayout(10, 10));
        setBackground(TRANG);
        setBorder(new EmptyBorder(15, 15, 15, 15));

        taoForm();
        taoBang();
        taoDuoi();
        loadData();
    }
    
    @Override
    public void refreshData(){
        loadData();
    }
    // ================== FORM NHẬP THÔNG TIN PHÒNG ==================
    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN PHÒNG CHIẾU", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        txtMa = tf(10); 
        txtMa.setEditable(false); 
        txtMa.setBackground(new Color(245, 245, 245));
        txtMa.setText("");

        txtTen = tf(25);

        cbSoGhe = new JComboBox<>(new String[]{"120", "132", "144", "156"});
        cbTrangThai = new JComboBox<>(new String[]{"Hoạt động", "Bảo trì", "Ngừng sử dụng"});
        cbManHinh = new JComboBox<>(new String[]{"2D", "3D", "IMAX", "4DX"});
        cbAmThanh = new JComboBox<>(new String[]{"Dolby Atmos", "Dolby 7.1", "Standard"});

        int y = 0;
        addRow(p, gbc, y++, "Mã phòng:", txtMa);
        addRow(p, gbc, y++, "Tên phòng:", txtTen);
        addRow(p, gbc, y++, "Số ghế:", cbSoGhe); 
        addRow(p, gbc, y++, "Trạng thái:", cbTrangThai);
        addRow(p, gbc, y++, "Màn hình:", cbManHinh);
        addRow(p, gbc, y++, "Âm thanh:", cbAmThanh);

        add(p, BorderLayout.NORTH);
    }

    // ================== BẢNG DANH SÁCH PHÒNG ==================
    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH PHÒNG CHIẾU", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(new String[]{
                "Mã Phòng", "Tên Phòng", "Số Ghế", "Trạng Thái", "Màn Hình", "Âm Thanh"
        }, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };

        table = new JTable(model);
        table.setRowHeight(50);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO); header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnCount(); i++) table.getColumnModel().getColumn(i).setHeaderRenderer(header);

        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                fillForm(table.getSelectedRow());
            }
        });

        p.add(new JScrollPane(table));
        add(p, BorderLayout.CENTER);
    }

    // ================== PHẦN DƯỚI: TÌM KIẾM + NÚT ==================
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
        cbTieuChi = new JComboBox<>(new String[]{"Mã phòng", "Tên phòng"});
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

        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { timKiem(); }
            public void removeUpdate(DocumentEvent e) { timKiem(); }
            public void changedUpdate(DocumentEvent e) { timKiem(); }
            private void timKiem() { timKiemNangCao(txtSearch.getText().trim()); }
        });

        // Nút chức năng 
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

    // ================== CRUD ==================
    private void them() {
        if (txtTen.getText().trim().isEmpty()) {
            msg("Vui lòng nhập tên phòng!");
            return;
        }

        PhongChieu pc = new PhongChieu();
        pc.setTenPhongChieu(txtTen.getText().trim());
        pc.setSoGheNgoi(Integer.parseInt((String) cbSoGhe.getSelectedItem()));
        pc.setTrangThaiPhong((String) cbTrangThai.getSelectedItem());
        pc.setLoaiManHinh((String) cbManHinh.getSelectedItem());
        pc.setHeThongAmThanh((String) cbAmThanh.getSelectedItem());

        if (dao.insert(pc)) {
            msg("Thêm phòng thành công! Đã tự động tạo ghế.");
            loadData();
            clearForm();
        } else {
            msg("Thêm thất bại!");
        }
    }

    private void sua() {
        if (txtMa.getText().isEmpty()) {
            msg("Vui lòng chọn phòng cần cập nhật!");
            return;
        }

        int maPhong = Integer.parseInt(txtMa.getText());

        PhongChieu pc = new PhongChieu();
        pc.setMaPhongChieu(maPhong);
        pc.setTenPhongChieu(txtTen.getText().trim());
        pc.setSoGheNgoi(Integer.parseInt((String) cbSoGhe.getSelectedItem()));
        pc.setTrangThaiPhong((String) cbTrangThai.getSelectedItem());
        pc.setLoaiManHinh((String) cbManHinh.getSelectedItem());
        pc.setHeThongAmThanh((String) cbAmThanh.getSelectedItem());

        if (dao.update(pc)) {
            msg("Cập nhật thành công! Ghế đã được điều chỉnh lại.");
            loadData();
            clearForm();
        } else {
            msg("Không thể cập nhật! Phòng này đang có suất chiếu.");
        }
    }

    private void xoa() {
        if (txtMa.getText().isEmpty()) {
            msg("Vui lòng chọn phòng cần xóa!");
            return;
        }

        int maPhong = Integer.parseInt(txtMa.getText());

        int confirm = thongBao("Xóa phòng chiếu này?\nTất cả ghế và suất chiếu sẽ bị xóa!", "Xác nhận", JOptionPane.QUESTION_MESSAGE, true);
        if (confirm == JOptionPane.YES_OPTION) {
            if (dao.delete(maPhong)) {
                msg("Xóa thành công!");
                loadData();
                clearForm();
            } else {
                msg("Không thể xóa! Phòng này đang có suất chiếu.");
            }
        }
    }

    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        cbSoGhe.setSelectedIndex(0);
        cbTrangThai.setSelectedIndex(0);
        cbManHinh.setSelectedIndex(0);
        cbAmThanh.setSelectedIndex(0);
        table.clearSelection();
    }

    // ================== LOAD + TÌM KIẾM ==================
    private void loadData() {
        model.setRowCount(0);
        List<PhongChieu> ds = dao.getAllPhongChieu();
        for (PhongChieu pc : ds) {
            model.addRow(new Object[]{
                pc.getMaPhongChieu(),
                pc.getTenPhongChieu(),
                pc.getSoGheNgoi(),
                pc.getTrangThaiPhong(),
                pc.getLoaiManHinh(),
                pc.getHeThongAmThanh()
            });
        }
    }

    private void timKiemNangCao(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            loadData();
            return;
        }
        keyword = keyword.trim().toLowerCase();
        model.setRowCount(0);
        List<PhongChieu> ds = dao.getAllPhongChieu();
        for (PhongChieu pc : ds) {
            boolean match = false;
            if (cbTieuChi.getSelectedIndex() == 0) {
                if (String.valueOf(pc.getMaPhongChieu()).contains(keyword)) match = true;
            } else {
                if (pc.getTenPhongChieu().toLowerCase().contains(keyword)) match = true;
            }
            if (match) {
                model.addRow(new Object[]{
                    pc.getMaPhongChieu(),
                    pc.getTenPhongChieu(),
                    pc.getSoGheNgoi(),
                    pc.getTrangThaiPhong(),
                    pc.getLoaiManHinh(),
                    pc.getHeThongAmThanh()
                });
            }
        }
    }

    private void fillForm(int row) {
        int ma = (int) model.getValueAt(row, 0);
        PhongChieu pc = dao.getById(ma);
        if (pc != null){
            txtMa.setText(String.valueOf(pc.getMaPhongChieu()));
            txtTen.setText(pc.getTenPhongChieu());
            cbSoGhe.setSelectedItem(String.valueOf(pc.getSoGheNgoi()));
            cbTrangThai.setSelectedItem(pc.getTrangThaiPhong());
            cbManHinh.setSelectedItem(pc.getLoaiManHinh());
            cbAmThanh.setSelectedItem(pc.getHeThongAmThanh());
        }
    }

    // ================== HÀM HỖ TRỢ ==================
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
        b.setPreferredSize(new Dimension(130, 45));
        b.setFocusPainted(false); b.setOpaque(true);
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
        b.setPreferredSize(new Dimension(110, 40));
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