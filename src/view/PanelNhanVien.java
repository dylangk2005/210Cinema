package view;

import dao.NhanVienDAO;
import model.NhanVien;
import com.toedter.calendar.JDateChooser;

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

 // ============= UI QUẢN LÝ NHÂN VIÊN ================

public class PanelNhanVien extends JPanel implements Refresh {
    // Màu sắc chủ đạo
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;
    
    // đinh dang in ngay
    private static final SimpleDateFormat SDF = new SimpleDateFormat("dd/MM/yyyy");
    
    // Thành phần UI
    private JTextField txtMaNV, txtHoTen, txtSDT, txtSearch;
    private JDateChooser dateChooser;
    private JComboBox<String> cbChucVu;
    private JComboBox<String> cbTieuChi;
    private JRadioButton rdNam, rdNu, rdKhac;
    private JTable table;
    private DefaultTableModel model;
    private final NhanVienDAO dao = new NhanVienDAO();
    
    // Khởi tạo UI
    public PanelNhanVien() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));
        setBackground(TRANG);
        taoForm();
        taoBang();
        taoDuoi();
        loadDuLieu();
    }
    
    @Override
    public void refreshData(){
        loadDuLieu();
    }
    
    // ============= 1. FORM NHẬP THÔNG TIN ================
    private void taoForm() {
        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(TRANG);
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "THÔNG TIN NHÂN VIÊN", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Các ô nhập
        txtMaNV = new JTextField(15);
        txtMaNV.setEditable(false); // k đc sửa mã nv
        txtMaNV.setBackground(new Color(245, 245, 245));

        txtHoTen = new JTextField(20);
        txtSDT = new JTextField(15);
        dateChooser = new JDateChooser();
        dateChooser.setDateFormatString("dd/MM/yyyy");

        cbChucVu = new JComboBox<>(new String[]{"Nhân viên bán vé", "Quản lý"});
        
        // radio giới tính
        rdNam = new JRadioButton("Nam", true);
        rdNu = new JRadioButton("Nữ");
        rdKhac = new JRadioButton("Khác");
        ButtonGroup bg = new ButtonGroup();
        bg.add(rdNam); bg.add(rdNu); bg.add(rdKhac);
        
        JPanel gender = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        gender.setBackground(TRANG);
        gender.add(rdNam); gender.add(rdNu);gender.add(rdKhac);
        
        // thêm từng dòng vào form
        int y = 0;
        themDong(p, gbc, y++, "Mã nhân viên:", txtMaNV);
        themDong(p, gbc, y++, "Họ và tên:", txtHoTen);
        themDong(p, gbc, y++, "Ngày sinh:", dateChooser);
        themDong(p, gbc, y++, "Giới tính:", gender);
        themDong(p, gbc, y++, "Số điện thoại:", txtSDT);
        themDong(p, gbc, y++, "Chức vụ:", cbChucVu);

        add(p, BorderLayout.NORTH);
    }
    
    // ============= 2. BẢNG DANH SÁCH NHÂN VIÊN ================
    private void taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH NHÂN VIÊN", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        String[] cols = {"Mã NV", "Họ tên", "Ngày sinh", "Giới tính", "SĐT", "Chức vụ"};
        model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        table = new JTable(model);
        table.setRowHeight(35);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        // Tô màu header
        DefaultTableCellRenderer headerRenderer = new DefaultTableCellRenderer();
        headerRenderer.setBackground(MAU_DO);
        headerRenderer.setForeground(TRANG);
        headerRenderer.setFont(new Font("Segoe UI", Font.BOLD, 15));
        headerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            table.getColumnModel().getColumn(i).setHeaderRenderer(headerRenderer);
        }

        JScrollPane scroll = new JScrollPane(table);
        p.add(scroll, BorderLayout.CENTER);
        
        // chọn,sửa 1 dòng -> tự điền vào form
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && table.getSelectedRow() >= 0) {
                loadFormTuBang(table.getSelectedRow());
            }
        });

        add(p, BorderLayout.CENTER);
    }
    
    // ============= 3. PANEL DƯỚI: TÌM KIẾM + CÁC NÚT THÊM MỚI, SỬA, XÓA, XÓA RỖNG ================
    private void taoDuoi() {
        JPanel duoi = new JPanel(new BorderLayout(20, 0));
        duoi.setBackground(TRANG);
        duoi.setBorder(new EmptyBorder(10, 0, 10, 0));

        // === Panel trái : Ô tìm kiếm + tiêu chí ===
        JPanel trai = new JPanel();
        trai.setLayout(new BoxLayout(trai, BoxLayout.Y_AXIS));
        trai.setBackground(TRANG);

        JPanel tieuChiPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        tieuChiPanel.setBackground(TRANG);
        tieuChiPanel.add(new JLabel("Tìm theo:"));
        cbTieuChi = new JComboBox<>(new String[]{"Mã nhân viên", "Họ tên", "Số điện thoại"});
        cbTieuChi.setPreferredSize(new Dimension(150, 32));
        tieuChiPanel.add(cbTieuChi);
        
        JPanel oTim = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        oTim.setBackground(TRANG);
        txtSearch = new JTextField(28);
        txtSearch.setPreferredSize(new Dimension(280, 32));
        
        // Nút làm mới (làm mới nội dung sau khi tìm kiếm)
        JButton btnRefresh = new JButton("Làm mới");
        btnRefresh.setBackground(new Color(108, 117, 125));     // xám đậm
        btnRefresh.setForeground(Color.WHITE);
        btnRefresh.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btnRefresh.setFocusPainted(false); // QUAN TRỌNG: tắt viền focus khi bấm
        btnRefresh.setBorder(BorderFactory.createEmptyBorder(8, 20, 8, 20)); // bo tròn nhẹ
        btnRefresh.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnRefresh.setPreferredSize(new Dimension(100, 32));
        
        btnRefresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(130, 140, 150)); // xám sáng khi hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnRefresh.setBackground(new Color(108, 117, 125)); // trở lại xám đậm
            }
        });

        btnRefresh.addActionListener(e -> {
            txtSearch.setText("");
            loadDuLieu();
        });

        oTim.add(txtSearch);
        oTim.add(btnRefresh);
        trai.add(tieuChiPanel);
        trai.add(oTim);
        
        // Tìm kiếm ngay khi gõ
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { tim(); }
            public void removeUpdate(DocumentEvent e) { tim(); }
            public void changedUpdate(DocumentEvent e) { tim(); }
            private void tim() {
                timKiemNangCao(txtSearch.getText().trim(), cbTieuChi.getSelectedIndex());
            }
        });

        // === Panel phải : các nút thêm, sửa, xóa, xóa rỗng ===
        JPanel phai = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 10));
        phai.setBackground(TRANG);

        JButton btnThem = nutDo("Thêm mới");
        JButton btnSua = nutDo("Cập nhật");
        JButton btnXoa = nutDo("Xóa");
        JButton btnClear = nutDo("Xóa rỗng");

        btnThem.addActionListener(e -> them());
        btnSua.addActionListener(e -> sua());
        btnXoa.addActionListener(e -> xoa());
        btnClear.addActionListener(e -> xoaForm());

        phai.add(btnThem); phai.add(btnSua); phai.add(btnXoa); phai.add(btnClear);

        duoi.add(trai, BorderLayout.WEST);
        duoi.add(phai, BorderLayout.EAST);
        add(duoi, BorderLayout.SOUTH);
    }
    // Hàm hỗ trợ tạo nút
    private JButton nutDo(String text) {
        JButton b = new JButton(text);
        b.setBackground(MAU_DO);
        b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 14));
        b.setFocusPainted(false);
        // hand cursor + hover
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent e) { b.setBackground(new Color(220, 0, 0)); }
            public void mouseExited(java.awt.event.MouseEvent e) { b.setBackground(MAU_DO); }
        });

        b.setPreferredSize(new Dimension(120, 40));
        return b;
    }
    
    // Hàm hỗ trợ thêm dòng vào form
    private void themDong(JPanel p, GridBagConstraints gbc, int y, String label, JComponent comp) {
        gbc.gridx = 0; gbc.gridy = y; gbc.weightx = 0.3;
        p.add(new JLabel(label), gbc);
        gbc.gridx = 1; gbc.weightx = 0.7;
        p.add(comp, gbc);
    }

    // ==================== 4. XỬ LÝ DỮ LIỆU ====================
    private void loadDuLieu() {
        timKiemNangCao("", 0);
    }

    private void timKiemNangCao(String keyword, int tieuChi) {
        model.setRowCount(0);
        List<NhanVien> ds = dao.timKiemNangCao(keyword, tieuChi);
        for (NhanVien nv : ds) {
            String ngaySinhStr = nv.getNgaySinh() != null ? SDF.format(nv.getNgaySinh()) : "Chưa xác định";
            model.addRow(new Object[]{
                nv.getMaNhanVien(),
                nv.getHoTenNhanVien(),
                ngaySinhStr,
                nv.getGioiTinh(),
                nv.getSoDienThoai(),
                nv.getMaChucVu() == 2 ? "Quản lý" : "Nhân viên bán vé"
            });
        }
    }
    
    // đưa dữ liệu từ bảng lên form khi click
    private void loadFormTuBang(int row) {
        txtMaNV.setText(model.getValueAt(row, 0).toString());
        txtHoTen.setText(model.getValueAt(row, 1).toString());

        int maNV = (int) model.getValueAt(row, 0);
        NhanVien nv = dao.getByID(maNV);

        if (nv != null && nv.getNgaySinh() != null) {
            dateChooser.setDate(nv.getNgaySinh());
        } else {
            dateChooser.setDate(null);
        }

        String gt = (String) model.getValueAt(row, 3);
        rdNam.setSelected("Nam".equals(gt));
        rdNu.setSelected("Nữ".equals(gt));

        txtSDT.setText(model.getValueAt(row, 4).toString());
        String cv = (String) model.getValueAt(row, 5);
        cbChucVu.setSelectedItem(cv);
    }
    
    // xóa trắng form khi click xóa rỗng
    private void xoaForm() {
        txtMaNV.setText(""); txtHoTen.setText(""); txtSDT.setText("");
        dateChooser.setDate(null); rdNam.setSelected(true);
        cbChucVu.setSelectedIndex(0); table.clearSelection();
    }
    
    // lấy thông tin từ form
    private NhanVien layTuForm() {
       
        String ten = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        java.util.Date d = dateChooser.getDate();
        if (ten.isEmpty() || sdt.isEmpty() || d == null) {
            throw new RuntimeException("Vui lòng nhập đầy đủ thông tin!");
        }
        int maCV = cbChucVu.getSelectedIndex() == 0 ? 1 : 2;
        NhanVien nv = new NhanVien(); 
        nv.setHoTenNhanVien(ten);
        nv.setNgaySinh(new Date(d.getTime()));
        nv.setGioiTinh(rdNam.isSelected() ? "Nam" : "Nữ");
        nv.setSoDienThoai(sdt);
        nv.setMaChucVu(maCV);
        return nv;
    }
    // kiểm tra form hop le
    private boolean validateForm() {
        java.util.List<String> errors = new java.util.ArrayList<>();

        String hoTen = txtHoTen.getText().trim();
        String sdt = txtSDT.getText().trim();
        java.util.Date ngaySinh = dateChooser.getDate();

        // 1. ht
        if (hoTen.isEmpty()) {
            errors.add("• Tên nhân viên không được để trống");
        } else if (!hoTen.matches("^[A-Za-zÀ-ỿà-ỹ\\s'-]+$")) {
            errors.add("• Tên nhân viên không được chứa số hoặc ký tự đặc biệt");
        }

        // 2. sdt
        if (sdt.isEmpty()) {
            errors.add("• Số điện thoại không được để trống");
        } else if (!sdt.matches("^0\\d{9,10}$")) {  
            errors.add("• Số điện thoại phải bắt đầu bằng 0 và chỉ chứa 10-11 chữ số");
        }
       
        // 3. ngay sinh
        if (ngaySinh == null) {
            errors.add("• Vui lòng chọn ngày sinh");
        } 

        // Hiển thị tất cả lỗi
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
    
    // ==================== 5. DIALOG THÔNG BÁO ==================== 
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
                btn.setBackground(new Color(139, 0, 0));
            }
        });
        return btn;
    }

     // ==================== 6. CÁC CHỨC NĂNG CHÍNH (THÊM/SỬA/XÓA) ==================== 
    private void them() {
        if (cbChucVu.getSelectedIndex() == 1) {
            thongBao("Không được thêm tài khoản QUẢN LÝ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        if (validateForm()){
            NhanVien nv = layTuForm();
            if (new NhanVienDAO().kiemTraTrungSDT(nv.getSoDienThoai(), -1)){
                thongBao("Trùng số điện thoại " + nv.getSoDienThoai() + ". Không thể thêm", "Thông báo", JOptionPane.INFORMATION_MESSAGE, false);
                return;
            }
            if (dao.add(nv)) {
                thongBao("Thêm thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadDuLieu(); xoaForm();
            }
        }
    }

    private void sua() {
        if (txtMaNV.getText().isEmpty()) {
            thongBao("Vui lòng chọn nhân viên để sửa!", "Thông báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        int row = table.getSelectedRow();
        if ("Quản lý".equals(model.getValueAt(row, 5))) {
            thongBao("Không được phép sửa tài khoản QUẢN LÝ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        if (cbChucVu.getSelectedIndex() == 1) {
            thongBao("Không được nâng cấp thành QUẢN LÝ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        if (validateForm()){
            NhanVien nv = layTuForm();
            int maNV = Integer.parseInt(txtMaNV.getText());
            nv.setMaNhanVien(maNV);
            if (new NhanVienDAO().kiemTraTrungSDT(nv.getSoDienThoai(), nv.getMaNhanVien())){
                thongBao("Trùng số điện thoại " + nv.getSoDienThoai() + ". Không thể cập nhật", "Thông báo", JOptionPane.INFORMATION_MESSAGE, false);
                return;
            }
            if (dao.update(nv)) {
                thongBao("Cập nhật thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadDuLieu();
                xoaForm();
            }
            else{
                thongBao("Lỗi. Không thể thêm!", "Lỗi", JOptionPane.INFORMATION_MESSAGE, false);
            }
        }
    }

    private void xoa() {
        int row = table.getSelectedRow();
        if (row == -1) {
            thongBao("Vui lòng chọn nhân viên để xóa!", "Thông báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        if ("Quản lý".equals(model.getValueAt(row, 5))) {
            thongBao("Không được xóa tài khoản QUẢN LÝ!", "Cảnh báo", JOptionPane.WARNING_MESSAGE, false);
            return;
        }
        int chon = thongBao("Bạn có chắc chắn muốn xóa nhân viên này?", "Xác nhận xóa", JOptionPane.QUESTION_MESSAGE, true);
        if (chon == JOptionPane.YES_OPTION) {
            if (dao.delete((int) model.getValueAt(row, 0))) {
                thongBao("Xóa thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE, false);
                loadDuLieu(); 
                xoaForm();
            }
        }
    }
}