package view;

import java.awt.*;
import javax.swing.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import util.PdfInvoiceGenerator;
import dao.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import model.*;

public class PanelThanhToan extends JPanel {
    private BigDecimal tienVe, tongTienSP, total, totalSauGiamGia;
    private int soGhe;
    private List<ChiTietHoaDon> ctlists = new ArrayList<>();
    private int maKHDaChon, diemTichLuy;
    private String phan_tram_giam;
    private boolean da_thanh_toan;
    private final Color RED = new Color(180, 0, 0);
    private final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 13);
    private final Font FONT_VALUE = new Font("Segoe UI", Font.PLAIN, 13);
  
    private JButton btnIn, btnInHD, btnHuy, btnXacNhan, btnCheck, btnSignup;
    private JTextField tfSDT, tfKhachDua;
    private JLabel lbGiamGia, lbTongTien, lbTraLai, lbKH_Ten, lbKH_GT, lbKH_Hang, lbKH_Diem;
    private JRadioButton rbTienMat, rbChuyenKhoan, rbTheTinDung;

    private boolean thanhToanThanhCong;  // đánh dấu thanh toán thành công , không cho in lại lần 2
    public PanelThanhToan(
            String lbTenPhim,   
            String lbTenPhong,
            String lbThoiGianBD,
            String lbGheDaChon,
            String lbGiaVe,
            JPanel listPanelSanPham,
            Set<Integer> listMaGhe,
            int maNhanVien,
            int maSuatChieu) {
        
        this.maKHDaChon = -1;
        this.phan_tram_giam = "0";
        this.da_thanh_toan = false;
        // Lop ngoai cung
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Lop trong, tren cung, ben trai
        JPanel infoVePanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoVePanel.setBackground(Color.WHITE);
        infoVePanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(RED, 1),
            "Thông tin vé",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            RED
        ));
        
        infoVePanel.add(makeLabel("Phim", lbTenPhim));
        infoVePanel.add(makeLabel("Phòng", lbTenPhong)); 
        infoVePanel.add(makeLabel("Suất chiếu", lbThoiGianBD)); 
        infoVePanel.add(makeLabel("Giá vé", lbGiaVe));
        
        JTextArea taGhe = new JTextArea(lbGheDaChon);
        taGhe.setEditable(false);
        taGhe.setFocusable(false);
        taGhe.setLineWrap(true);
        taGhe.setWrapStyleWord(true);

        JScrollPane scroll = new JScrollPane(
            taGhe,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        scroll.setBorder(null);
        scroll.setPreferredSize(new Dimension(100, 16));

        infoVePanel.add(makeRow("Ghế đã chọn", scroll));
        
        // Lop trong, tren cung, ben phai
        JPanel spPanelContainer = new JPanel(new BorderLayout());
        spPanelContainer.setBackground(Color.WHITE);
        spPanelContainer.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(RED, 1),
            "Thông tin sản phẩm",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            RED
        ));
        
        JPanel spPanel = new JPanel();
        spPanel.setLayout(new BoxLayout(spPanel, BoxLayout.Y_AXIS));
        spPanel.setBackground(Color.WHITE);

        tongTienSP = BigDecimal.ZERO;
        for (Component c : listPanelSanPham.getComponents()) {
            if (c instanceof JPanel productPanel) {

                JLabel lb = (JLabel) productPanel.getComponent(0);
                JLabel dg = (JLabel) productPanel.getComponent(1);          
                JSpinner sl = (JSpinner) productPanel.getComponent(2);

                int soLuong = (int) sl.getValue();
                if (soLuong > 0) {
                    BigDecimal price = parseMoney(dg.getText()).multiply(new BigDecimal(soLuong));
                    tongTienSP = tongTienSP.add(price);
                    JLabel item = new JLabel(
                        "<html><b>" + lb.getText() +
                        "</b> x" + soLuong +
                        " = <span style='font-weight:bold;color:#C80000'>" + formatMoney(price) + "</span></html>"
                    );
                    item.setFont(FONT_VALUE);
                    spPanel.add(item);
                    ChiTietHoaDon ct = new ChiTietHoaDon();
                    ct.setMaSanPham((int) productPanel.getClientProperty("product"));
                    ct.setDonGiaLucBan(price);
                    ctlists.add(ct);
                }
            }
        }
        
        JScrollPane spScroll = new JScrollPane(spPanel);
        spScroll.setBorder(null);
        spScroll.setPreferredSize(new Dimension(0, 200));
        spScroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        spPanelContainer.add(spScroll, BorderLayout.CENTER);
        
        // Lop trong tren cung, boc infoVePanel va spPanel
        JPanel panelTop = new JPanel(new GridLayout(1, 2, 15, 0));
        panelTop.setBackground(Color.WHITE);
        panelTop.setPreferredSize(new Dimension(0, 200));
        panelTop.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        panelTop.add(infoVePanel);
        panelTop.add(spPanelContainer);
        
        // Lop trong, o giua, boc giamGiaPanel va accountPanel
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        
        // Lop trong, o giua, nam tren
        JPanel giamGiaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 4));
        giamGiaPanel.setBackground(Color.WHITE);
        giamGiaPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        tfSDT = new JTextField(12);
        btnCheck = new JButton("Áp mã");
        btnSignup = new JButton("Đăng ký thành viên");
        lbGiamGia = new JLabel("Giảm giá: 0%");
        
        styleButton(btnCheck, new Color(80, 80, 80));
        styleButton(btnSignup, new Color(180,0,0));
        btnCheck.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnCheck.setBackground(new Color(110, 110, 110));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnCheck.setBackground(new Color(80, 80, 80));
            }
        });
        btnSignup.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnSignup.setBackground(new Color(220, 0, 0));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnSignup.setBackground(new Color(180, 0, 0));
            }
        }); 
        giamGiaPanel.add(new JLabel("SĐT (mã giảm giá): "));
        giamGiaPanel.add(tfSDT);
        giamGiaPanel.add(btnCheck);
        giamGiaPanel.add(btnSignup);
        
        // Lop trong, o giua, nam duoi
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new GridLayout(4, 1, 0, 3));
        accountPanel.setBackground(Color.WHITE);
        
        accountPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(RED, 1),
            "Thông tin khách hàng",
            0, 0,
            new Font("Segoe UI", Font.BOLD, 14),
            RED
        ));
        
        accountPanel.setAlignmentX(Component.LEFT_ALIGNMENT);

        lbKH_Ten = makeLabel("Tên khách hàng", "(null)");
        lbKH_GT = makeLabel("Giới tính", "null");
        lbKH_Hang = makeLabel("Hạng thành viên", "null");
        lbKH_Diem = makeLabel("Điểm tích lũy", "null");

        accountPanel.add(lbKH_Ten);
        accountPanel.add(lbKH_GT);
        accountPanel.add(lbKH_Hang);
        accountPanel.add(lbKH_Diem);
        
        // Them giamGiaPanel va accountPanel vao panelCenter
        panelCenter.add(giamGiaPanel);
        panelCenter.add(accountPanel);
        
        soGhe = 0;
        if (!lbGheDaChon.equals("-")) {
            soGhe = lbGheDaChon.split(",").length;
        }
        tienVe = parseMoney(lbGiaVe).multiply(new BigDecimal(soGhe)); 
        total = tongTienSP.add(tienVe);
        
        totalSauGiamGia = total;
        
        lbTongTien = new JLabel("Tổng tiền: " + formatMoney(total));  
        lbTongTien.setFont(new Font("SegoeUI", Font.BOLD, 16));
        lbTongTien.setForeground(RED);
        
        // Lop duoi cung, nam tren
        JPanel totalPanel = new JPanel(new GridBagLayout());
        totalPanel.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        // ====== DÒNG 1: Giảm giá ======
        gbc.gridx = 0; gbc.gridy = 0;
        totalPanel.add(lbGiamGia, gbc);

        // ====== DÒNG 2: Tổng tiền ======
        gbc.gridx = 0; gbc.gridy = 1;
        totalPanel.add(lbTongTien, gbc);

        // ====== DÒNG 3: Khách đưa ======
        tfKhachDua = new JTextField("", 10);
        lbTraLai = new JLabel(formatMoney(totalSauGiamGia.negate())); 
        
        JPanel khachDuaPanel = new JPanel(new GridLayout(1, 2));
        khachDuaPanel.setBackground(Color.WHITE);
        khachDuaPanel.add(tfKhachDua);
        khachDuaPanel.add(new JLabel(" đ"));

        gbc.gridx = 0; gbc.gridy = 2;
        totalPanel.add(new JLabel("Khách đưa:"), gbc);

        gbc.gridx = 1;
        totalPanel.add(khachDuaPanel, gbc);

        // ====== DÒNG 4: Trả lại ======
        gbc.gridx = 0; gbc.gridy = 3;
        totalPanel.add(new JLabel("Trả lại:"), gbc);

        gbc.gridx = 1;
        totalPanel.add(lbTraLai, gbc);

        // ====== DÒNG 5: Hình thức ======
        rbTienMat = new JRadioButton("Tiền mặt");
        rbChuyenKhoan = new JRadioButton("Chuyển khoản");
        rbTheTinDung = new JRadioButton("Thẻ tín dụng");

        rbTienMat.setBackground(Color.WHITE);
        rbChuyenKhoan.setBackground(Color.WHITE);
        rbTheTinDung.setBackground(Color.WHITE);

        ButtonGroup payGroup = new ButtonGroup();
        payGroup.add(rbTienMat); 
        payGroup.add(rbChuyenKhoan);
        payGroup.add(rbTheTinDung);
        rbTienMat.setSelected(true);

        JPanel paymentPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        paymentPanel.setBackground(Color.WHITE);
        paymentPanel.add(rbTienMat);
        paymentPanel.add(rbChuyenKhoan);
        paymentPanel.add(rbTheTinDung);

        gbc.gridx = 0; gbc.gridy = 4;
        totalPanel.add(new JLabel("Hình thức:"), gbc);

        gbc.gridx = 1;
        totalPanel.add(paymentPanel, gbc);

        btnHuy = new JButton("Hủy");
        btnXacNhan = new JButton("Xác nhận thanh toán");
        btnIn = new JButton("In vé");
        btnInHD = new JButton("In hóa đơn");
        
        styleButton(btnHuy, Color.DARK_GRAY);
        styleButton(btnIn, new Color(80, 80, 80));
        styleButton(btnInHD, new Color(80, 80, 80));
        styleButton(btnXacNhan, new Color(180, 0, 0));
        btnHuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHuy.setBackground(new Color(110, 110, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHuy.setBackground(Color.DARK_GRAY);
            }
        });
        btnInHD.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnInHD.setBackground(new Color(110, 110, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnInHD.setBackground(new Color(80, 80, 80));
            }
        });
        btnXacNhan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnXacNhan.setBackground(new Color(220, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnXacNhan.setBackground(new Color(180, 0, 0));
            }
        });
        btnIn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnIn.setBackground(new Color(110, 110, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnIn.setBackground(new Color(80, 80, 80));
            }
        });
        // Lop duoi cung, nam duoi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.setBorder(BorderFactory.createEmptyBorder(20, 10, 0, 10)); 
        actionPanel.add(btnHuy);
        actionPanel.add(btnIn);
        actionPanel.add(btnInHD);
        actionPanel.add(btnXacNhan);
        
        // Lop duoi cung, boc totalPanel va actionPanel
        JPanel panelBottom = new JPanel(new BorderLayout());
        panelBottom.setBackground(Color.WHITE);
        panelBottom.add(totalPanel, BorderLayout.NORTH);
        panelBottom.add(actionPanel, BorderLayout.SOUTH);
              
        // Gan het vao panelThanhToan
        this.add(panelTop, BorderLayout.NORTH);
        this.add(panelCenter, BorderLayout.CENTER);
        this.add(panelBottom, BorderLayout.SOUTH);
        
        // ==== EVENT ACTION ==== 
        
        
        tfKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { tinhTienTraLai(); }

            @Override
            public void removeUpdate(DocumentEvent e) { tinhTienTraLai(); }

            @Override
            public void changedUpdate(DocumentEvent e) { tinhTienTraLai(); }

            private void tinhTienTraLai() {
                try {
                    BigDecimal khachDua = parseMoney(tfKhachDua.getText());
                    BigDecimal traLai = khachDua.subtract(totalSauGiamGia);
                    lbTraLai.setText(formatMoney(traLai)); 
                } catch (Exception ex) {
                    lbTraLai.setText("0 đ");
                }
            }
        });

        btnIn.addActionListener(e -> {
            if(lbGheDaChon.trim().equals("-")) {
                msg("Khách hàng không đặt vé! Không thể in vé", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String[] suat = lbThoiGianBD.split(" ");
                String ticketInfo = String.format("Ticket: %s | Tax: 0,000 đ", lbGiaVe);
                PdfInvoiceGenerator.exportTicketCGVPdf(
                        lbTenPhim,
                        suat[1],
                        suat[0],
                        lbTenPhong,
                        lbGheDaChon,
                        ticketInfo
                );
            } catch (Exception Ex) {
                    Ex.printStackTrace();
            }
            msg("In vé thành công!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnInHD.addActionListener(e -> {
            if (!da_thanh_toan) {
                msg("Không thể in hóa đơn! Vui lòng thanh toán hóa đơn trước!", "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                String hinhThuc;
                if (rbTheTinDung.isSelected()) hinhThuc = "Thẻ tín dụng";
                else if (rbChuyenKhoan.isSelected()) hinhThuc = "Chuyển khoản";
                else hinhThuc = "Tiền mặt";
                String[] khachHangInfos = new String[]{
                    lbKH_Ten.getText().replaceAll("<[^>]*>", ""), 
                    lbKH_GT.getText().replaceAll("<[^>]*>", ""), 
                    tfSDT.getText(),
                    lbKH_Diem.getText().replaceAll("<[^>]*>", ""), 
                    hinhThuc
                };
                
                String[] thanhToanInfos = new String[]{
                    formatMoney(total), 
                    lbGiamGia.getText(), 
                    tfKhachDua.getText() + " đ", 
                    lbTraLai.getText(), 
                    formatMoney(totalSauGiamGia)
                };
                List<String[]> items = new ArrayList<>();           
                int idx = 1;
                // số ghế > 0 : thêm trường vé
                if (soGhe > 0) {
                    items.add(new String[]{
                        String.valueOf(idx++), 
                        "Vé xem phim", 
                        "Vé",
                        String.valueOf(soGhe), 
                        lbGiaVe, 
                        formatMoney(tienVe)
                    });
                }    
                for (Component c : listPanelSanPham.getComponents()) {
                    if (c instanceof JPanel productPanel) {
                        JLabel lb = (JLabel) productPanel.getComponent(0);   // tên sản phẩm
                        JLabel dg = (JLabel) productPanel.getComponent(1);       // đơn giá
                        JSpinner sl = (JSpinner) productPanel.getComponent(2); // số lượng

                        int soLuong = (int) sl.getValue();
                        if (soLuong > 0) {
                            String temp = dg.getText();
                            BigDecimal don_gia = parseMoney(temp);
                            BigDecimal thanh_tien = don_gia.multiply(new BigDecimal(soLuong));

                            items.add(new String[]{
                                String.valueOf(idx++),
                                lb.getText(),
                                "SP",
                                String.valueOf(soLuong),
                                formatMoney(don_gia),
                                formatMoney(thanh_tien)
                            });
                        }
                    }
                }
                PdfInvoiceGenerator.exportVATInvoice(items, khachHangInfos, thanhToanInfos); 
                
            } catch (Exception Ex) {
                Ex.printStackTrace();
            }
            msg("In hóa đơn thành công!", "Thông báo",  JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnHuy.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }); 
        
        btnXacNhan.addActionListener(e -> {
            
            if (thanhToanThanhCong) {
                msg("Hóa đơn này đã được thanh toán rồi!\nKhông thể thanh toán lại.", 
                    "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            BigDecimal num = parseMoney(lbTraLai.getText());
            if(num.compareTo(BigDecimal.ZERO) < 0) {
               msg("Không thể thanh toán! Vui lòng thanh toán đủ tiền!", "Lỗi",  JOptionPane.ERROR_MESSAGE);
               return;
            } 
            try {
                updateDatabase(listMaGhe, maNhanVien, maSuatChieu, lbGiaVe);
            } catch (Exception ex) {
                System.getLogger(PanelThanhToan.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            msg("Thanh toán thành công", "Thông báo!", JOptionPane.INFORMATION_MESSAGE);
            this.da_thanh_toan = true;
            thanhToanThanhCong = true;
        });
        // check SDT nhap vao, ap giam gia va cap nhat tich luy
        btnCheck.addActionListener(e -> {
            this.maKHDaChon = -1;
            this.phan_tram_giam = "0";
            String sdt = tfSDT.getText().trim();
            KhachHang kh = new KhachHangDAO().getKhachHangBySDT(sdt);
            if (kh == null) {
                lbKH_Ten.setText("Tên khách hàng: -");
                lbKH_GT.setText("Giới tính: -"); 
                lbKH_Hang.setText("Hạng: -");
                lbKH_Diem.setText("Điểm tích lũy: -");
                lbTongTien.setText("Tổng tiền: " + formatMoney(total)); 
                totalSauGiamGia = total;
                lbGiamGia.setText("Giảm giá: 0%"); 
                msg("Bạn chưa phải thành viên!", "Thông báo",  JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            this.maKHDaChon = kh.getMaKhachHang();
            String hang = kh.getHangThanhVien().trim();
            int diemTL = kh.getDiemTichLuy();
            lbKH_Ten.setText("Tên khách hàng: " + kh.getHoTenKhachHang());
            lbKH_GT.setText("Giới tính: " + kh.getGioiTinh()); 
            lbKH_Hang.setText("Hạng: " + hang);
            lbKH_Diem.setText("Điểm tích lũy: " + diemTL);
            this.diemTichLuy = diemTL + soGhe * 10;
            if (hang.equalsIgnoreCase("Bạc")) {
                BigDecimal tienVeGiam = tienVe.multiply(new BigDecimal("0.05"));
                BigDecimal tienSPGiam = tongTienSP.multiply(new BigDecimal("0.05"));
                totalSauGiamGia = total.subtract(tienVeGiam).subtract(tienSPGiam);
                lbGiamGia.setText("Giảm giá: 5% tiền vé + 5% tiền sản phẩm"); 
                lbTongTien.setText("Tổng tiền: " + formatMoney(totalSauGiamGia)); 
                this.phan_tram_giam = "0.05";
            } else if (hang.equalsIgnoreCase("Vàng")) {
                BigDecimal tienVeGiam = tienVe.multiply(new BigDecimal("0.1"));
                BigDecimal tienSPGiam = tongTienSP.multiply(new BigDecimal("0.1"));
                totalSauGiamGia = total.subtract(tienVeGiam).subtract(tienSPGiam);
                lbGiamGia.setText("Giảm giá: 10% tiền vé + 10% tiền sản phẩm"); 
                lbTongTien.setText("Tổng tiền: " + formatMoney(totalSauGiamGia)); 
                this.phan_tram_giam = "0.1";
            } else if (hang.equalsIgnoreCase("Kim cương")) {
                BigDecimal tienVeGiam = tienVe.multiply(new BigDecimal("0.15"));
                BigDecimal tienSPGiam = tongTienSP.multiply(new BigDecimal("0.15"));
                totalSauGiamGia = total.subtract(tienVeGiam).subtract(tienSPGiam);
                lbGiamGia.setText("Giảm giá: 15% tiền vé + 15% tiền sản phẩm"); 
                lbTongTien.setText("Tổng tiền: " + formatMoney(totalSauGiamGia)); 
                this.phan_tram_giam = "0.15";
            } else {
                lbTongTien.setText("Tổng tiền: " + formatMoney(total)); 
                totalSauGiamGia = total;
                lbGiamGia.setText("Giảm giá: 0%"); 
                msg("Bạn không được giảm giá!", "Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btnSignup.addActionListener(e -> {
            PanelDangKiKhachHang dialog = new PanelDangKiKhachHang(
                (JDialog) SwingUtilities.getWindowAncestor(PanelThanhToan.this),
                maKH -> this.maKHDaChon = maKH
            );
            dialog.setVisible(true);
        });
        
    }    
    
    private BigDecimal parseMoney(String text) {
        if (text == null || text.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        text = text.trim();
        boolean isNegative = text.startsWith("-") || text.startsWith("−"); 
        String cleaned = text.replaceAll("[^\\d]", "");
        if (cleaned.isEmpty()) return BigDecimal.ZERO;
        BigDecimal result = new BigDecimal(cleaned);
        if (isNegative) result = result.negate();
        return result;
    }
    
    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0 đ";
        DecimalFormat df = new DecimalFormat("#,###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(amount) + " đ";
    }
  
    private JLabel makeLabel(String title, String value) {
        JLabel lb = new JLabel("<html><b>" + title + ":</b> <span style='font-weight: normal;'>" + value + "</span></html>");
        lb.setFont(FONT_VALUE);
        return lb;
    }
    
    private JPanel makeRow(String title, JComponent valueComp) {
        JPanel row = new JPanel(new GridBagLayout());
        row.setBackground(Color.WHITE);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST; 
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tạo tiêu đề
        JLabel lbTitle = new JLabel(title + ":");
        lbTitle.setFont(FONT_TITLE);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0;
        row.add(lbTitle, gbc);

        // Component giá trị
        valueComp.setFont(FONT_VALUE);
        valueComp.setOpaque(true);
        valueComp.setBackground(Color.WHITE);
        valueComp.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 0));

        gbc.gridx = 1;
        gbc.weightx = 1;
        row.add(valueComp, gbc);

        return row;
    }

    
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15)); 
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }
    
    private void updateDatabase(Set<Integer> listMaGhe, 
            int maNhanVienDaChon, 
            int maSuatChieu,
            String gia_ve) throws Exception {
       
        
        String hinhThucThanhToan;
        if (rbTienMat.isSelected()) {
            hinhThucThanhToan = "Tiền mặt";
        } else if (rbChuyenKhoan.isSelected()) {
            hinhThucThanhToan = "Chuyển khoản";
        } else {
            hinhThucThanhToan = "Thẻ tín dụng";
        }
        
        HoaDon dh = new HoaDon(maNhanVienDaChon, maKHDaChon, totalSauGiamGia, hinhThucThanhToan);
        int maHoaDon = new HoaDonDAO().insertHoaDon(dh);
//        int diem_tl = 1400;
        new KhachHangDAO().updateDiemTichLuy(this.maKHDaChon, this.diemTichLuy); 
//        new KhachHangDAO().updateDiemTichLuy(this.maKHDaChon, diem_tl); 
     
        Set<Integer> listMaVe = new HashSet<>();
        if (listMaGhe != null){
            listMaVe = new VeDAO().insertVe(maSuatChieu, listMaGhe);
        }
        for (ChiTietHoaDon ct : ctlists) {
            BigDecimal don_gia_luc_ban = ct.getDonGiaLucBan();
            BigDecimal tien_giam = don_gia_luc_ban.multiply(new BigDecimal(phan_tram_giam));
            BigDecimal thanh_tien = don_gia_luc_ban.subtract(tien_giam);
            ct.setMaHoaDon(maHoaDon); ct.setMaVe(-1); ct.setSoLuong(1); 
            ct.setThanhTien(thanh_tien); 
        }
        
        for (Integer maVe : listMaVe) {
            BigDecimal don_gia_luc_ban = parseMoney(gia_ve);
            BigDecimal tien_giam = don_gia_luc_ban.multiply(new BigDecimal(phan_tram_giam));
            BigDecimal thanh_tien = don_gia_luc_ban.subtract(tien_giam);
            ctlists.add(new ChiTietHoaDon(maHoaDon, -1, maVe, 1, don_gia_luc_ban, thanh_tien)); 
        }
        
        new ChiTietHoaDonDAO().insertChiTietHoaDon(ctlists); 
    }
    // tạo nút dialog
    public void msg(String message, String title, int type) {
        // Tạo nút OK đỏ 
        JButton btnOK = new JButton("OK");
        btnOK.setBackground(new Color(180, 0, 0));
        btnOK.setForeground(Color.WHITE);
        btnOK.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btnOK.setFocusPainted(false);
        btnOK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOK.setBorder(BorderFactory.createEmptyBorder(10, 35, 10, 35));
        btnOK.setPreferredSize(new Dimension(90, 30));

        // Hover effect
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOK.setBackground(new Color(220, 0, 0));
            }
            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOK.setBackground(new Color(180, 0, 0));
            }
        });

        // Tạo JOptionPane chỉ có 1 nút
        JOptionPane optionPane = new JOptionPane(
            message,
            type,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{btnOK},
            btnOK
        );

        // Tạo dialog
        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setResizable(false);

        // Bấm nút OK → đóng dialog
        btnOK.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }
}
