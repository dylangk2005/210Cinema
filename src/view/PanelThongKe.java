package view;

import dao.ThongKeDAO;
import model.ThongKe;
import com.toedter.calendar.JDateChooser;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.Color;
import java.awt.Font;
import java.io.FileOutputStream;
import java.sql.Date;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

// ================= GIAO DIỆN THỐNG KÊ & BÁO CÁO DOANH THU ======================
public class PanelThongKe extends JPanel {
    // Màu sắc
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;
    
    // Kết nối + Định dạng
    private final ThongKeDAO dao = new ThongKeDAO();
    private final DecimalFormat df = new DecimalFormat("###,### VNĐ");
    private final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    // Các bảng & model
    private DefaultTableModel modelDoanhThu, modelTopPhim, modelNhanVien, modelSanPham;
    private JTable tableDoanhThu, tableTopPhim, tableNhanVien, tableSanPham;

    // control cho tab doanh thu
    private JDateChooser dcTu, dcDen;
    private JComboBox<String> cbLoaiThoiGian;
    private JComboBox<String> cbThangTu, cbThangDen, cbNamTu, cbNamDen;
    private JComboBox<String> cbNamTu2, cbNamDen2;
    
    // Khởi tạo giao diện
    public PanelThongKe() {
        setLayout(new BorderLayout());
        setBackground(TRANG);
        setBorder(new EmptyBorder(20, 20, 20, 20));

        JLabel title = new JLabel("THỐNG KÊ & BÁO CÁO DOANH THU", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 32));
        title.setForeground(MAU_DO);
        add(title, BorderLayout.NORTH);

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Segoe UI", Font.BOLD, 16));
        tabbedPane.setForeground(MAU_DO);

        tabbedPane.addTab("Doanh thu theo thời gian", taoTabDoanhThu());
        tabbedPane.addTab("Doanh thu nhân viên", taoTabNhanVien());
        tabbedPane.addTab("Top phim ăn khách", taoTabTopPhim());
        tabbedPane.addTab("Top sản phẩm bán chạy", taoTabSanPham());

        add(tabbedPane, BorderLayout.CENTER);
    }

    // ==================== 1. TAB DOANH THU THEO THỜI GIAN ====================
    private JPanel taoTabDoanhThu() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(TRANG);

        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBackground(TRANG);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Loại thống kê
        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Xem theo:"), gbc);
        cbLoaiThoiGian = new JComboBox<>(new String[]{"Theo ngày", "Theo tháng", "Theo năm"});
        cbLoaiThoiGian.setPreferredSize(new Dimension(150, 40));
        gbc.gridx = 1;
        topPanel.add(cbLoaiThoiGian, gbc);

        // === Panel chọn ngày ===
        JPanel panelNgay = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelNgay.setBackground(TRANG);
        panelNgay.add(new JLabel("Từ:"));
        dcTu = new JDateChooser(); dcTu.setDateFormatString("dd/MM/yyyy");
        dcTu.setPreferredSize(new Dimension(150, 40));
        panelNgay.add(dcTu);
        panelNgay.add(new JLabel("Đến:"));
        dcDen = new JDateChooser(); dcDen.setDateFormatString("dd/MM/yyyy");
        dcDen.setPreferredSize(new Dimension(150, 40));
        panelNgay.add(dcDen);

        // === Panel chọn tháng ===
        JPanel panelThang = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelThang.setBackground(TRANG);
        panelThang.add(new JLabel("Từ tháng:"));
        cbThangTu = new JComboBox<>(); cbThangDen = new JComboBox<>();
        cbNamTu = new JComboBox<>(); cbNamDen = new JComboBox<>();
        for (int m = 1; m <= 12; m++) {
            String mm = String.format("%02d", m);
            cbThangTu.addItem(mm); cbThangDen.addItem(mm);
        }
        int namHienTai = Calendar.getInstance().get(Calendar.YEAR);
        for (int y = namHienTai - 10; y <= namHienTai + 5; y++) {
            String year = String.valueOf(y);
            cbNamTu.addItem(year); cbNamDen.addItem(year);
        }
        
        cbThangTu.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        cbNamTu.setSelectedItem(String.valueOf(namHienTai));
        cbThangDen.setSelectedIndex(Calendar.getInstance().get(Calendar.MONTH));
        cbNamDen.setSelectedItem(String.valueOf(namHienTai));
        
        panelThang.add(cbThangTu); panelThang.add(cbNamTu);
        panelThang.add(Box.createHorizontalStrut(20));
        panelThang.add(new JLabel("Đến tháng:"));
        panelThang.add(cbThangDen); panelThang.add(cbNamDen);

        // === Panel chọn năm ===
        JPanel panelNam = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        panelNam.setBackground(TRANG);
        panelNam.add(new JLabel("Từ năm:"));
        cbNamTu2 = new JComboBox<>(); cbNamDen2 = new JComboBox<>();
        
        for (int y = namHienTai - 20; y <= namHienTai + 5; y++) {
            cbNamTu2.addItem(String.valueOf(y));
            cbNamDen2.addItem(String.valueOf(y));
        }
        
        cbNamTu2.setSelectedItem(String.valueOf(namHienTai));
        cbNamDen2.setSelectedItem(String.valueOf(namHienTai));
        panelNam.add(cbNamTu2);
        panelNam.add(Box.createHorizontalStrut(20));
        panelNam.add(new JLabel("Đến năm:"));
        panelNam.add(cbNamDen2);

        // Buttons
        JButton btnXem = nutDo("Xem báo cáo");
        JButton btnExcel = nutDo("Xuất Excel");
        
        // Đặt các panel vào gridbag(chồng lên nhau)
        gbc.gridx = 2; gbc.gridwidth = 3;
        topPanel.add(panelNgay, gbc);
        topPanel.add(panelThang, gbc);
        topPanel.add(panelNam, gbc);
        gbc.gridx = 5; gbc.gridwidth = 1;
        topPanel.add(btnXem, gbc);
        gbc.gridx = 6;
        topPanel.add(btnExcel, gbc);
        
        // Ẩn các panel (click vào tab sẽ hiện)
        panelThang.setVisible(false);
        panelNam.setVisible(false);
        
        // Đổi loại thống kê + hiện panel tương ứng
        cbLoaiThoiGian.addActionListener(e -> {
            panelNgay.setVisible(false); panelThang.setVisible(false); panelNam.setVisible(false);
            int idx = cbLoaiThoiGian.getSelectedIndex();
            
            if (idx == 0) panelNgay.setVisible(true);
            else if (idx == 1) panelThang.setVisible(true);
            else panelNam.setVisible(true);
          
            topPanel.revalidate(); topPanel.repaint();
        });

        // Bảng hiển thị doanh thu
        modelDoanhThu = new DefaultTableModel(new String[]{"Thời gian", "Số hóa đơn", "Tổng doanh thu (VNĐ)"}, 0) {
           @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableDoanhThu = taoBang(modelDoanhThu);

        // Nút Xem báo cáo: kiểm tra thời gian trước khi load
        btnXem.addActionListener(e -> {
            if (!kiemTraThoiGian()) return;
            xemDoanhThu();
        });

        // Nút Xuất Excel: kiểm tra thời gian trước khi xuất
        btnExcel.addActionListener(e -> {
            if (modelDoanhThu.getRowCount() == 0) {
                thongBao("Chưa có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            if (!kiemTraThoiGian()) return;
            xuatExcel(modelDoanhThu, tableDoanhThu, "DoanhThu_TheoThoiGian");
        });

        p.add(topPanel, BorderLayout.NORTH);
        p.add(new JScrollPane(tableDoanhThu), BorderLayout.CENTER);
        return p;
    }


    // ==================== 2. TAB TOP PHIM ====================
    private JPanel taoTabTopPhim() {
        return taoTabCoXepHang(
                "Top 10 phim ăn khách",
                "top_phim",
                dao::getTopPhim,
                new String[]{"Xếp hạng", "Tên phim", "Số vé bán", "Doanh thu (VNĐ)"}
        );
    }

    // ==================== 3. TAB DOANH THU NHÂN VIÊN ====================
    private JPanel taoTabNhanVien() {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(TRANG);
        
        JPanel top = new JPanel(new FlowLayout());
        top.setBackground(TRANG);
        
        JButton btnXem = nutDo("Xem báo cáo");
        JButton btnExcel = nutDo("Xuất Excel");
        
        top.add(btnXem); top.add(btnExcel);

        modelNhanVien = new DefaultTableModel(new String[]{
                "Họ tên nhân viên", "Số hóa đơn", "Tổng doanh thu (VNĐ)"
        }, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        tableNhanVien = taoBang(modelNhanVien);
        btnXem.addActionListener(e -> loadData(modelNhanVien, dao::getDoanhThuNhanVien, false));
        btnExcel.addActionListener(e -> xuatExcel(modelNhanVien, tableNhanVien, "Doanhthu_nhanvien"));
        
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(tableNhanVien), BorderLayout.CENTER);
        return p;
    }

    // ==================== 4. TAB TOP SẢN PHẨM ====================
    private JPanel taoTabSanPham() {
        return taoTabCoXepHang(
                "Top sản phẩm đồ ăn bán chạy",
                "top_sanpham",
                dao::getSanPhamBanChay,
                new String[]{"Xếp hạng", "Tên sản phẩm", "Số lượng", "Doanh thu (VNĐ)"}
        );
    }

    // ==================== 5. HÀM CHUNG CHO TAB CÓ XẾP HẠNG (TOP PHIM, TOP SP) ====================
    private JPanel taoTabCoXepHang(String tieuDe, String tenFileExcel,
                                   java.util.function.Supplier<List<ThongKe>> supplier,
                                   String[] cols) {
        JPanel p = new JPanel(new BorderLayout(10, 10));
        p.setBackground(TRANG);
        JPanel top = new JPanel(new FlowLayout());
        top.setBackground(TRANG);
        JButton btnXem = nutDo("Xem báo cáo");
        JButton btnExcel = nutDo("Xuất Excel");
        top.add(btnXem); top.add(btnExcel);
        
        DefaultTableModel model = new DefaultTableModel(cols, 0) {
            @Override public boolean isCellEditable(int r, int c) { return false; }
        };
        
        JTable table = taoBang(model);
        btnXem.addActionListener(e -> loadData(model, supplier, true));
        
        btnExcel.addActionListener(e -> {
            if (model.getRowCount() == 0) {
                thongBao("Chưa có dữ liệu để xuất!", "Thông báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            xuatExcel(model, table, tenFileExcel);
        });
        
        p.add(top, BorderLayout.NORTH);
        p.add(new JScrollPane(table), BorderLayout.CENTER);
        return p;
    }

    // ==================== 6.LOAD DỮ LIỆU VÀO BẢNG  ====================
    private void loadData(DefaultTableModel model,
                          java.util.function.Supplier<List<ThongKe>> supplier,
                          boolean coXepHang) {
        model.setRowCount(0);
        List<ThongKe> ds = supplier.get();
        int stt = 1;
        for (ThongKe tk : ds) {
            if (coXepHang) {
                model.addRow(new Object[]{stt++, tk.getTen(), tk.getSoLuong(), df.format(tk.getDoanhThu())});
            } else {
                model.addRow(new Object[]{tk.getTen(), tk.getSoLuong(), df.format(tk.getDoanhThu())});
            }
        }
    }

    // ==================== CÁC HÀM HỖ TRỢ ====================
    private JTable taoBang(DefaultTableModel model) {
        JTable t = new JTable(model);
        t.setRowHeight(40);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 15));
        t.getTableHeader().setBackground(MAU_DO);
        t.getTableHeader().setForeground(TRANG);
        t.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 16));
        t.setSelectionBackground(new Color(255, 220, 220));
        return t;
    }
    
    // Hàm tạo btn
    private JButton nutDo(String text) {
        JButton b = new JButton(text);
        b.setBackground(MAU_DO);
        b.setForeground(TRANG);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setPreferredSize(new Dimension(180, 50));
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.setFocusPainted(false);
        return b;
    }
    
    // Hàm lấy dữ liệu doanh thu và hiển thị lên bảng
    private void xemDoanhThu() {
        int idx = cbLoaiThoiGian.getSelectedIndex();
        modelDoanhThu.setRowCount(0);

        if (idx == 0) { // theo ngày
            java.util.Date uTu = dcTu.getDate();
            java.util.Date uDen = dcDen.getDate();
            if (uTu == null || uDen == null) {
                thongBao("Vui lòng chọn đầy đủ ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (uTu.after(uDen)) {
                thongBao("Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            Date sqlTu = new Date(uTu.getTime());
            Date sqlDen = new Date(uDen.getTime());
            List<ThongKe> ds = dao.getDoanhThuTheoThoiGian(idx, sqlTu, sqlDen);
            for (ThongKe tk : ds) {
                modelDoanhThu.addRow(new Object[]{tk.getTen(), tk.getSoLuong(), df.format(tk.getDoanhThu())});
            }
            return;
        }

        if (idx == 1) { // theo tháng
            int thangTu = Integer.parseInt(cbThangTu.getSelectedItem().toString());
            int namTu = Integer.parseInt(cbNamTu.getSelectedItem().toString());
            int thangDen = Integer.parseInt(cbThangDen.getSelectedItem().toString());
            int namDen = Integer.parseInt(cbNamDen.getSelectedItem().toString());
         
            Date sqlTu = Date.valueOf(String.format("%04d-%02d-01", namTu, thangTu));
            Date sqlDen = Date.valueOf(String.format("%04d-%02d-28", namDen, thangDen)); // approximate end
            List<ThongKe> ds = dao.getDoanhThuTheoThoiGian(idx, sqlTu, sqlDen);
            for (ThongKe tk : ds) {
                modelDoanhThu.addRow(new Object[]{tk.getTen(), tk.getSoLuong(), df.format(tk.getDoanhThu())});
            }
            return;
        }

        // theo năm
        int nTu = Integer.parseInt(cbNamTu2.getSelectedItem().toString());
        int nDen = Integer.parseInt(cbNamDen2.getSelectedItem().toString());
        Date sqlTu = Date.valueOf(String.format("%04d-01-01", nTu));
        Date sqlDen = Date.valueOf(String.format("%04d-12-31", nDen));
        List<ThongKe> ds = dao.getDoanhThuTheoThoiGian(2, sqlTu, sqlDen);
        for (ThongKe tk : ds) {
            modelDoanhThu.addRow(new Object[]{tk.getTen(), tk.getSoLuong(), df.format(tk.getDoanhThu())});
        }
    }

    // kiểm tra thời gian có hợp lệ hay không trc khi xem/xuất bào cáo
    private boolean kiemTraThoiGian() {
        int idx = cbLoaiThoiGian.getSelectedIndex();
        if (idx == 0) { // ngày: kiểm tra có chọn đủ
            java.util.Date uTu = dcTu.getDate();
            java.util.Date uDen = dcDen.getDate();
            if (uTu == null || uDen == null) {
                thongBao("Vui lòng chọn đầy đủ ngày!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            if (uTu.after(uDen)) {
                thongBao("Ngày bắt đầu phải nhỏ hơn hoặc bằng ngày kết thúc!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        if (idx == 1) { // tháng: năm/ tháng sau phải lớn hơn hoặc bằng
            int thangTu = Integer.parseInt(cbThangTu.getSelectedItem().toString());
            int namTu = Integer.parseInt(cbNamTu.getSelectedItem().toString());
            int thangDen = Integer.parseInt(cbThangDen.getSelectedItem().toString());
            int namDen = Integer.parseInt(cbNamDen.getSelectedItem().toString());
            if (namDen < namTu || (namDen == namTu && thangDen < thangTu)) {
                thongBao("Thời gian kết thúc phải lớn hơn hoặc bằng thời gian bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return false;
            }
            return true;
        }
        // năm
        int n1 = Integer.parseInt(cbNamTu2.getSelectedItem().toString());
        int n2 = Integer.parseInt(cbNamDen2.getSelectedItem().toString());
        if (n2 < n1) {
            thongBao("Năm kết thúc phải lớn hơn hoặc bằng năm bắt đầu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }
    // Lấy chuỗi thời gian để ghi vào file excel (Tiêu đề thời gian)
    private String layChuoiThoiGian() {
        int idx = cbLoaiThoiGian.getSelectedIndex();
        if (idx == 0) { // ngày
            java.util.Date uTu = dcTu.getDate();
            java.util.Date uDen = dcDen.getDate();
            if (uTu == null || uDen == null) return "";
            return sdf.format(uTu) + "  -  " + sdf.format(uDen);
        }
        if (idx == 1) { // tháng
            return cbThangTu.getSelectedItem() + "/" + cbNamTu.getSelectedItem()
                    + "  -  "
                    + cbThangDen.getSelectedItem() + "/" + cbNamDen.getSelectedItem();
        }
        return cbNamTu2.getSelectedItem() + "  -  " + cbNamDen2.getSelectedItem();
    }
    
    // Tạo tên file Excel theo nội dung
    private String resolveFileName(String inputKey) {

        if (inputKey == null) return "baocao";
        
        String key = inputKey.toLowerCase();
        if (key.contains("doanhthu_theothoigian") || key.contains("doanhthu")) {
            int idx = cbLoaiThoiGian != null ? cbLoaiThoiGian.getSelectedIndex() : -1;
            if (idx == 0) return "Doanhthu_ngay";
            if (idx == 1) return "Doanhthu_thang";
            if (idx == 2) return "Doanhthu_nam";
            return "Doanhthu";
        }
        if (key.contains("top10_phim") || key.contains("top_phim")) return "top_phim";
        if (key.contains("doanhthu_nhanvien") || key.contains("nhanvien")) return "Doanhthu_nhanvien";
        if (key.contains("top_sanpham") || key.contains("sanpham")) return "top_sanpham";
        return inputKey.replace(" ", "_");
    }
    
    // Hàm xuất báo cáo ra file excel (.xls)
    private void xuatExcel(DefaultTableModel model, JTable table, String inputKey) {
    try (Workbook wb = new HSSFWorkbook()) {
        Sheet sheet = wb.createSheet("Báo cáo");
        int rowIndex = 0;

        // tiêu đề
        CellStyle styleTitle = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font fontTitle = wb.createFont();
        fontTitle.setBold(true);
        fontTitle.setFontHeightInPoints((short) 13);
        styleTitle.setFont(fontTitle);
        styleTitle.setAlignment(HorizontalAlignment.CENTER);

        //thời gian
        CellStyle styleTime = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font fontTime = wb.createFont();
        fontTime.setBold(false);
        fontTime.setFontHeightInPoints((short) 12);
        styleTime.setFont(fontTime);
        styleTime.setAlignment(HorizontalAlignment.CENTER);

        // header bảng
        CellStyle styleHeader = wb.createCellStyle();
        org.apache.poi.ss.usermodel.Font fontHeader = wb.createFont();
        fontHeader.setBold(true);
        styleHeader.setFont(fontHeader);
        styleHeader.setBorderBottom(BorderStyle.THIN);
        styleHeader.setBorderTop(BorderStyle.THIN);
        styleHeader.setBorderLeft(BorderStyle.THIN);
        styleHeader.setBorderRight(BorderStyle.THIN);
        styleHeader.setAlignment(HorizontalAlignment.CENTER);

        CellStyle styleCell = wb.createCellStyle();
        styleCell.setBorderBottom(BorderStyle.THIN);
        styleCell.setBorderTop(BorderStyle.THIN);
        styleCell.setBorderLeft(BorderStyle.THIN);
        styleCell.setBorderRight(BorderStyle.THIN);

        // Dòng 1 : Tiêu đề
        Row rowTitle = sheet.createRow(rowIndex++);
        Cell cellTitle = rowTitle.createCell(0);
        String tieuDe = inputKey;
        
        if ("DoanhThu_TheoThoiGian".equalsIgnoreCase(inputKey) || inputKey.toLowerCase().contains("doanhthu")) {
            tieuDe = "210CINEMA - BÁO CÁO DOANH THU";
        } else if (inputKey.toLowerCase().contains("top") && inputKey.toLowerCase().contains("phim")) {
            tieuDe = "210CINEMA - TOP PHIM ĂN KHÁCH";
        } else if (inputKey.toLowerCase().contains("sanpham")) {
            tieuDe = "210CINEMA - TOP SẢN PHẨM BẢN CHẠY";
        } else if (inputKey.toLowerCase().contains("nhanvien")) {
            tieuDe = "210CINEMA - DOANH THU NHÂN VIÊN";
        }
        
        cellTitle.setCellValue(tieuDe);
        cellTitle.setCellStyle(styleTitle);
        sheet.addMergedRegion(new CellRangeAddress(0, 0, 0, model.getColumnCount() - 1));

        // Dòng 2: Thời gian
        Row rowTime = sheet.createRow(rowIndex++);
        Cell cellTime = rowTime.createCell(0);
        cellTime.setCellValue("Thời gian: " + (cbLoaiThoiGian != null ? layChuoiThoiGian() : ""));
        cellTime.setCellStyle(styleTime);
        sheet.addMergedRegion(new CellRangeAddress(1, 1, 0, model.getColumnCount() - 1));

        rowIndex++; // 1 dòng trống

        // Header bảng
        Row rowHeader = sheet.createRow(rowIndex++);
        for (int col = 0; col < model.getColumnCount(); col++) {
            Cell cell = rowHeader.createCell(col);
            cell.setCellValue(model.getColumnName(col));
            cell.setCellStyle(styleHeader);
        }

        // Dữ liệu
        for (int i = 0; i < model.getRowCount(); i++) {
            Row row = sheet.createRow(rowIndex++);
            for (int j = 0; j < model.getColumnCount(); j++) {
                Cell cell = row.createCell(j);
                Object value = model.getValueAt(i, j);
                cell.setCellValue(value == null ? "" : value.toString());
                cell.setCellStyle(styleCell);
            }
        }

        // Tự động giãn cột
        for (int i = 0; i < model.getColumnCount(); i++) sheet.autoSizeColumn(i);

        // Tên file
        String fileName = resolveFileName(inputKey) + ".xls";
        try (FileOutputStream out = new FileOutputStream(fileName)) {
            wb.write(out);
        }

        thongBao("Xuất Excel thành công!\nFile: " + fileName, "Thành công", JOptionPane.INFORMATION_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            thongBao("Lỗi xuất Excel: " + ex.getMessage(), "Lỗi", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Dialog thông báo
    private void thongBao(String msg, String title, int type) {
  
        JButton btnDongY = new JButton("OK");
        btnDongY.setBackground(new Color(139, 0, 0));     // đỏ đậm
        btnDongY.setForeground(Color.WHITE);
        btnDongY.setFont(new Font("SegoeUI", Font.BOLD, 14));
        btnDongY.setFocusPainted(false);
        btnDongY.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnDongY.setBorder(BorderFactory.createEmptyBorder(12, 40, 12, 40));
        btnDongY.setPreferredSize(new Dimension(110, 30));

        // hover
        btnDongY.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDongY.setBackground(new Color(200, 0, 0)); // đỏ sáng khi hover
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDongY.setBackground(new Color(139, 0, 0)); // trở lại đỏ đậm
            }
        });

        // Tạo JOptionPane chỉ có 1 nút
        JOptionPane optionPane = new JOptionPane(
            msg,
            type,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{btnDongY}, 
            btnDongY
        );

        // Tạo dialog
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setResizable(false);

        // Bấm nút → đóng dialog
        btnDongY.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}