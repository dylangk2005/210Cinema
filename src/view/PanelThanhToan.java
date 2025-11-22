package view;

import dao.KhachHangDAO;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Window;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import util.PdfInvoiceGenerator;
import model.KhachHang;

public class PanelThanhToan extends JPanel {
    private BigDecimal tienVe, tongTienSP, total, totalSauGiamGia;
    private int soGhe;
    private PaymentListener listener;
    
    public interface PaymentListener {
        void onRequestOpenCustomerPanel();
    }
    
    public void setPaymentListener(PaymentListener l) {
        this.listener = l;
    }
    
    public PanelThanhToan(
            JLabel lbTenPhim,
            JLabel lbTenPhong,
            JLabel lbThoiGianBD,
            JLabel lbGheDaChon,
            JLabel lbGiaVe,
            JPanel listPanelSanPham) {
        // Lop ngoai cung
        this.setLayout(new BorderLayout(15, 15));
        this.setBackground(Color.WHITE);
        this.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        // Lop trong, tren cung, ben trai
        JPanel infoVePanel = new JPanel(new GridLayout(5, 1, 5, 5));
        infoVePanel.setBackground(Color.WHITE);
        infoVePanel.setBorder(BorderFactory.createTitledBorder(
                "Thông tin vé"
        ));
        infoVePanel.add(new JLabel("Phim: " + lbTenPhim.getText()));
        infoVePanel.add(new JLabel("Phòng: " + lbTenPhong.getText()));
        infoVePanel.add(new JLabel("Suất chiếu: " + lbThoiGianBD.getText()));
        infoVePanel.add(new JLabel("Ghế đã chọn: " + lbGheDaChon.getText()));
        infoVePanel.add(new JLabel("Giá vé: " + lbGiaVe.getText()));
        
        // Lop trong, tren cung, ben phai
        JPanel spPanelContainer = new JPanel(new BorderLayout());
        spPanelContainer.setBackground(Color.WHITE);
        spPanelContainer.setBorder(BorderFactory.createTitledBorder("Thông tin sản phẩm"));
        JPanel spPanel = new JPanel();
        spPanel.setLayout(new BoxLayout(spPanel, BoxLayout.Y_AXIS));
        spPanel.setBackground(Color.WHITE);

        tongTienSP = BigDecimal.ZERO;
        for (Component c : listPanelSanPham.getComponents()) {
            if (c instanceof JPanel productPanel) {

                JLabel lb = (JLabel) productPanel.getComponent(0);
                JLabel dg = (JLabel) productPanel.getComponent(1);          
                JTextField tf = (JTextField) productPanel.getComponent(2);

                int soLuong = Integer.parseInt(tf.getText().trim());
                if (soLuong > 0) {
                    BigDecimal price = parseMoney(dg.getText()).multiply(new BigDecimal(soLuong));
                    tongTienSP = tongTienSP.add(price);
                    JLabel item = new JLabel(lb.getText() +
                            "  x" + soLuong +
                            "  = " + 
                            formatMoney(price));
                    spPanel.add(item);
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
        panelTop.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panelTop.add(infoVePanel);
        panelTop.add(spPanelContainer);
        
        // Lop trong, o giua, boc giamGiaPanel va accountPanel
        JPanel panelCenter = new JPanel();
        panelCenter.setLayout(new BoxLayout(panelCenter, BoxLayout.Y_AXIS));
        panelCenter.setBackground(Color.WHITE);
        panelCenter.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); 
        panelCenter.setMaximumSize(new Dimension(Integer.MAX_VALUE, 180));
        
        // Lop trong, o giua, nam tren
        JPanel giamGiaPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        giamGiaPanel.setBackground(Color.WHITE);
        giamGiaPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); 

        JTextField tfSDT = new JTextField(12);
        JButton btnCheck = new JButton("Áp mã");
        JButton btnSignup = new JButton("Đăng ký thành viên");
        JLabel lbGiamGia = new JLabel("Giảm giá: 0%");
        giamGiaPanel.add(new JLabel("SĐT (mã giảm giá): "));
        giamGiaPanel.add(tfSDT);
        giamGiaPanel.add(btnCheck);
        giamGiaPanel.add(btnSignup);
        
        // Lop trong, o giua, nam duoi
        JPanel accountPanel = new JPanel();
        accountPanel.setLayout(new GridLayout(3, 1, 5, 5));
        accountPanel.setBackground(Color.WHITE);
        accountPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                "Thông tin khách hàng"
        ));

        JLabel lbKH_Ten = new JLabel("Tên khách hàng: (chưa có)");
        JLabel lbKH_GT = new JLabel("Giới tính: -");
        JLabel lbKH_Hang = new JLabel("Hạng thành viên: -");
        JLabel lbKH_Diem = new JLabel("Điểm tích lũy: -");

        accountPanel.add(lbKH_Ten);
        accountPanel.add(lbKH_GT);
        accountPanel.add(lbKH_Hang);
        accountPanel.add(lbKH_Diem);
        
        // Them giamGiaPanel va accountPanel vao panelCenter
        panelCenter.add(giamGiaPanel);
        JPanel gap = new JPanel();
        gap.setPreferredSize(new Dimension(0, 5));
        gap.setBackground(Color.WHITE);
        panelCenter.add(gap);
        panelCenter.add(accountPanel);
        
        soGhe = 0;
        if (!lbGheDaChon.getText().equals("-")) {
            soGhe = lbGheDaChon.getText().split(",").length;
        }
        tienVe = parseMoney(lbGiaVe.getText()).multiply(new BigDecimal(soGhe)); 
        total = tongTienSP.add(tienVe);
        JLabel lbTongTien = new JLabel("Tổng tiền: " + formatMoney(total));  
        lbTongTien.setFont(new Font("Arial", Font.BOLD, 16));
        // Lop duoi cung, nam tren
        JPanel totalPanel = new JPanel(new GridLayout(2, 1));
        totalPanel.setBackground(Color.WHITE);
        totalPanel.add(lbGiamGia);
        totalPanel.add(lbTongTien);
        
        JButton btnHuy = new JButton("Hủy");
        JButton btnXacNhan = new JButton("Xác nhận thanh toán");
        
        // Lop duoi cung, nam duoi
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        actionPanel.setBackground(Color.WHITE);
        actionPanel.add(btnHuy);
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
        btnHuy.addActionListener(e -> {
            Window window = SwingUtilities.getWindowAncestor(this);
            if (window != null) {
                window.dispose();
            }
        }); 
        
        btnXacNhan.addActionListener(e -> {
            showDialogThanhToan();
        });
        // check SDT nhap vao, ap giam gia va cap nhat tich luy
        btnCheck.addActionListener(e -> {
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
                JOptionPane.showMessageDialog(this,"Bạn chưa phải thành viên!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
                return;
            }
            String hang = kh.getHangThanhVien().trim();
            int diemTichLuy = kh.getDiemTichLuy();
            lbKH_Ten.setText("Tên khách hàng: " + kh.getHoTenKhachHang());
            lbKH_GT.setText("Giới tính: " + kh.getGioiTinh()); 
            lbKH_Hang.setText("Hạng: " + hang);
            lbKH_Diem.setText("Điểm tích lũy: " + diemTichLuy);
            if (hang.equalsIgnoreCase("Bạc")) {
                BigDecimal tienVeGiam = tienVe.multiply(new BigDecimal("0.05"));
                BigDecimal tienSPGiam = tongTienSP.multiply(new BigDecimal("0.05"));
                totalSauGiamGia = total.subtract(tienVeGiam).subtract(tienSPGiam);
                lbGiamGia.setText("Giảm giá: 5% tiền vé + 5% tiền sản phẩm"); 
                lbTongTien.setText("Tổng tiền: " + formatMoney(totalSauGiamGia)); 
            } else if (hang.equalsIgnoreCase("Vàng")) {
                BigDecimal tienVeGiam = tienVe.multiply(new BigDecimal("0.1"));
                BigDecimal tienSPGiam = tongTienSP.multiply(new BigDecimal("0.1"));
                totalSauGiamGia = total.subtract(tienVeGiam).subtract(tienSPGiam);
                lbGiamGia.setText("Giảm giá: 10% tiền vé + 10% tiền sản phẩm"); 
                lbTongTien.setText("Tổng tiền: " + formatMoney(totalSauGiamGia)); 
            } else if (hang.equalsIgnoreCase("Kim cương")) {
                BigDecimal tienVeGiam = tienVe.multiply(new BigDecimal("0.15"));
                BigDecimal tienSPGiam = tongTienSP.multiply(new BigDecimal("0.15"));
                totalSauGiamGia = total.subtract(tienVeGiam).subtract(tienSPGiam);
                lbGiamGia.setText("Giảm giá: 15% tiền vé + 15% tiền sản phẩm"); 
                lbTongTien.setText("Tổng tiền: " + formatMoney(totalSauGiamGia)); 
            } else {
                lbTongTien.setText("Tổng tiền: " + formatMoney(total)); 
                totalSauGiamGia = total;
                lbGiamGia.setText("Giảm giá: 0%"); 
                JOptionPane.showMessageDialog(this,"Bạn không được giảm giá!","Thông báo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        
        btnSignup.addActionListener(e -> {
            if (listener != null) {
                listener.onRequestOpenCustomerPanel();
            }
            listener.onRequestOpenCustomerPanel();

            Window window = SwingUtilities.getWindowAncestor(PanelThanhToan.this);
            if (window instanceof JDialog) {
                ((JDialog) window).dispose();
            }
        });
        
    }    
    
    private BigDecimal parseMoney(String text) {
        if (text == null || text.trim().isEmpty()) {
            return BigDecimal.ZERO;
        }
        String cleaned = text.replaceAll("[^\\d]", "");
        if (cleaned.isEmpty()) return BigDecimal.ZERO;
        return new BigDecimal(cleaned);
    }
    
    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0 đ";
        DecimalFormat df = new DecimalFormat("#,###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(amount) + " đ";
    }
    
    public void showDialogThanhToan() {

        Window window = SwingUtilities.getWindowAncestor(this);
        JDialog dialog;
        if (window instanceof Frame) {
            dialog = new JDialog((Frame) window, "Xác nhận thanh toán", true);
        } else if (window instanceof Dialog) {
            dialog = new JDialog((Dialog) window, "Xác nhận thanh toán", true);
        } else {
            // fallback: nếu không xác định được owner
            dialog = new JDialog();
        }
        dialog.setSize(400, 260);
        dialog.setLocationRelativeTo(this);

        // ====== Tiêu đề ======
        JLabel title = new JLabel("BẠN CÓ MUỐN THANH TOÁN KHÔNG?", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        dialog.add(title, BorderLayout.NORTH);

        JPanel center = new JPanel(new GridBagLayout());
        center.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5); 
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        JLabel lbTongTien = new JLabel(formatMoney(totalSauGiamGia));
        JTextField tfKhachDua = new JTextField("0");
        JLabel lbTraLai = new JLabel("0 đ");
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        center.add(new JLabel("Tổng tiền:"), gbc);
        gbc.gridx = 1;
        center.add(lbTongTien, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 1;             
        center.add(new JLabel("Khách đưa:"), gbc);
        gbc.gridx = 1;
        tfKhachDua.setColumns(10);
        center.add(tfKhachDua, gbc);
        
        gbc.gridx = 0;
        gbc.gridy = 2;
        center.add(new JLabel("Trả lại:"), gbc);
        gbc.gridx = 1;
        center.add(lbTraLai, gbc);

        dialog.add(center, BorderLayout.CENTER);

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 10));
        JButton btnIn = new JButton("In hóa đơn");
        JButton btnThanhToan = new JButton("Thanh toán");
        JButton btnHuy = new JButton("Hủy");
        
        buttons.add(btnIn);
        buttons.add(btnThanhToan);
        buttons.add(btnHuy);

        dialog.add(buttons, BorderLayout.SOUTH);
        
        tfKhachDua.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                tinhTienTraLai();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                tinhTienTraLai();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                tinhTienTraLai();
            }

            private void tinhTienTraLai() {
                try {
                    BigDecimal khachDua = new BigDecimal(tfKhachDua.getText());
                    BigDecimal traLai = khachDua.subtract(totalSauGiamGia);
                    lbTraLai.setText(formatMoney(traLai));
                } catch (NumberFormatException ex) {
                    lbTraLai.setText("0 đ");
                }
            }
        });

        btnIn.addActionListener(e -> {
            exportPDF();
            JOptionPane.showMessageDialog(dialog, "In bill thành công!");
            dialog.dispose();
        });

        btnThanhToan.addActionListener(e -> {
            updateDatabase();
            JOptionPane.showMessageDialog(dialog, "Thanh toán thành công!");
            dialog.dispose();
        });

        btnHuy.addActionListener(e -> dialog.dispose());

        dialog.setVisible(true);
    }   
    
    private void exportPDF() {
        try {
            PdfInvoiceGenerator.exportMovieTicketPdf(
                    "hoadon.pdf",
                    "Avengers: Endgame",
                    "26/04/2019",
                    "19:30 - 22:32",
                    "3 giờ 2 phút",
                    "CGV Vincom Gò Vấp",
                    "Cinema 3",
                    List.of("C4", "C3"),
                    List.of("Bắp lớn", "Nước siêu lớn"),
                    180000,
                    90000,
                    0,
                    270000
            );
        } catch (Exception e) {
                e.printStackTrace();
        }
    }
    private void updateDatabase() {}
}
