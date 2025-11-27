package view;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import dao.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Set;
import model.*;

public class PanelBanHang extends JPanel implements Refresh {

    private JComboBox<Phim> cbPhim;
    private JComboBox<PhongChieu> cbPhong;
    private JComboBox<SuatChieu> cbSuatChieu;
    private JLabel lbTenPhim, lbTenPhong, lbThoiLuong, lbTheLoai, lbThoiGianBD, lbGiaVe;
    private JTextArea taGheDaChon;
    private JPanel listPanel;
    private JButton btnGioHang, btnXacNhan, btnChonGhe, btnLamMoi;
    private int maPhongChieuDaChon, maPhimDaChon, maSuatChieuDaChon, maNhanVien;
    private Set<Integer> listMaGheDaChon;

    public PanelBanHang(int maNV) {
        this.maNhanVien = maNV;
        this.maPhongChieuDaChon = this.maPhimDaChon = this.maSuatChieuDaChon = -1;
        initUI();
        initEvents();
    }
    
    private void initUI() {
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);
        add(createTopPanel(), BorderLayout.NORTH);
        add(createCenterPanel(), BorderLayout.CENTER);
        add(createBottomPanel(), BorderLayout.SOUTH);
    }
    
    @Override
    public void refreshData(){
        clearForm();
        cbPhim.removeAllItems();
        cbPhong.removeAllItems();
        cbPhim.addItem(null);
        cbPhong.addItem(null);
        List<Phim> danhSachPhim = new PhimDAO().selectAll();
        for (Phim p : danhSachPhim) {
            cbPhim.addItem(p);
        }
        
        List<PhongChieu> danhSachPhongChieu = new PhongChieuDAO().getAllPhongChieu();
        for (PhongChieu p : danhSachPhongChieu) {
            cbPhong.addItem(p);
        }
    }
    
    private void initEvents() {
        cbPhim.addActionListener(e -> {
            Phim p = (Phim) cbPhim.getSelectedItem();
            if (p != null) {
                lbTenPhim.setText(p.getTenPhim());
                lbThoiLuong.setText(p.getThoiLuong() + " phút");
                lbTheLoai.setText(p.getTheLoai());
                maPhimDaChon = p.getMaPhim();
            } else {
                lbTenPhim.setText("-");
                lbThoiLuong.setText("-");
                lbTheLoai.setText("-");
                maPhimDaChon = -1;
            }
            if (maPhimDaChon != -1 && maPhongChieuDaChon != -1) {
                cbSuatChieu.removeAllItems();
                cbSuatChieu.addItem(null); 
                List<SuatChieu> danhSachSuatChieu = new SuatChieuDAO().getSuatChieuByPhimAndPhong(maPhimDaChon, maPhongChieuDaChon);
                for (SuatChieu sc : danhSachSuatChieu) {
                    cbSuatChieu.addItem(sc);
                }       
            } else {
                cbSuatChieu.removeAllItems();
                cbSuatChieu.addItem(null); 
            }
        });

        cbPhong.addActionListener(e -> {
            PhongChieu p = (PhongChieu) cbPhong.getSelectedItem();
            if (p != null) {
                lbTenPhong.setText(p.getTenPhongChieu());
                maPhongChieuDaChon = p.getMaPhongChieu();
            } else {
                lbTenPhong.setText("-");
                maPhongChieuDaChon = -1;
            }
            if (maPhimDaChon != -1 && maPhongChieuDaChon != -1) {
                cbSuatChieu.removeAllItems();
                cbSuatChieu.addItem(null); 
                List<SuatChieu> danhSachSuatChieu = new SuatChieuDAO().getSuatChieuByPhimAndPhong(maPhimDaChon, maPhongChieuDaChon);
                for (SuatChieu sc : danhSachSuatChieu) {
                    cbSuatChieu.addItem(sc);
                }       
            } else {
                cbSuatChieu.removeAllItems();
                cbSuatChieu.addItem(null); 
            }
        });

        cbSuatChieu.addActionListener(e -> {
            SuatChieu p = (SuatChieu) cbSuatChieu.getSelectedItem();
            if (p != null) {
                lbThoiGianBD.setText(p.toString());
                lbGiaVe.setText(formatMoney(p.getGiaVeCoBan())); 
                maSuatChieuDaChon = p.getMaSuatChieu();
            } else {
                lbThoiGianBD.setText("-");
                lbGiaVe.setText("-"); 
                maSuatChieuDaChon = -1;
            }
            
        });

        btnChonGhe.addActionListener(e -> {
             if (maPhimDaChon == -1 || maPhongChieuDaChon == -1 || maSuatChieuDaChon == -1) {
                JOptionPane.showMessageDialog(this, 
                    "Vui lòng chọn đầy đủ Phim → Phòng → Suất chiếu trước khi chọn ghế!", 
                    "Chưa chọn đủ thông tin", JOptionPane.WARNING_MESSAGE);
                return;
            }
            
            PanelChonGhe dialog = new PanelChonGhe(
                maPhongChieuDaChon,    
                maSuatChieuDaChon,    
                (JFrame) SwingUtilities.getWindowAncestor(this),
                (seats, listMaGhe) -> {
                    taGheDaChon.setText(String.join(", ", seats));
                    this.listMaGheDaChon = listMaGhe;
                }
            );
            
            dialog.setVisible(true);
        });        
        
        btnXacNhan.addActionListener(e -> {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this),
                                         "Thanh toán", true);
            dialog.setLayout(new BorderLayout());
            dialog.setSize(650, 700);
            dialog.setLocationRelativeTo(this);

            PanelThanhToan panelThanhToan = new PanelThanhToan(
                    lbTenPhim.getText(),
                    lbTenPhong.getText(),
                    lbThoiGianBD.getText(),
                    taGheDaChon.getText(),
                    lbGiaVe.getText(),
                    listPanel,
                    listMaGheDaChon,
                    maNhanVien,
                    maSuatChieuDaChon
            );

            dialog.add(panelThanhToan);
            dialog.setVisible(true);
        });      
        
        btnGioHang.addActionListener(e -> {
            List<JPanel> listPanelVe = new ArrayList<>();
            for (String seatcode : taGheDaChon.getText().split(",")) {
                JPanel panel = createVeCard(seatcode);
                listPanelVe.add(panel);
            }

            PanelGioHang dlg = new PanelGioHang(
                    (JFrame) SwingUtilities.getWindowAncestor(this),
                    listPanelVe,
                    listPanel,
                    btnXacNhan);
            dlg.setVisible(true);
        });
    }
    
// =============== HAM TAO PANEL TOP ==================    
    private JPanel createTopPanel() {
        // CAC LABEL
        JLabel lbPhim = new JLabel("Phim:");
        JLabel lbPhong = new JLabel("Phòng:");
        JLabel lbSuat = new JLabel("Suất chiếu:");
        
        // btnChonGhe
        btnChonGhe = makeButton(120, 36, new Color(200, 0, 0), "CHỌN GHẾ");
           
        // COMBOBOX PHIM
        cbPhim = new JComboBox<>();
        cbPhim.addItem(null);
        
        // COMBOBOX PHONGCHIEU
        cbPhong = new JComboBox<>();
        cbPhong.addItem(null);
        
        // COMBOBOX SUATCHIEU
        cbSuatChieu = new JComboBox<>();
        cbSuatChieu.addItem(null);
        
        // LOAD NOI DUNG VAO COMBOBOX
        List<Phim> danhSachPhim = new PhimDAO().selectAll();
        for (Phim p : danhSachPhim) {
            cbPhim.addItem(p);
        }
        
        List<PhongChieu> danhSachPhongChieu = new PhongChieuDAO().getAllPhongChieu();
        for (PhongChieu p : danhSachPhongChieu) {
            cbPhong.addItem(p);
        }
        
        // SET PLACEHOLDER
        cbPhim.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                      Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value == null) {
                    setText("-- Chọn phim --");
                }

                return this;
            }
        });
        
        cbPhong.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                      Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    setText("-- Chọn phòng --");
                }
                return this;
            }
        });
        
        cbSuatChieu.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list,
                      Object value, int index, boolean isSelected, boolean cellHasFocus) {

                super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

                if (value == null) {
                    setText("-- Chọn suất --");
                }
                return this;
            }
        });
        
        JPanel topPanel = new JPanel(new GridBagLayout());
        
        topPanel.setBorder(BorderFactory.createTitledBorder("CHỌN PHIM VÀ SUẤT CHIẾU"));
        topPanel.setBackground(Color.WHITE);        

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        gbc.gridx = 0; gbc.gridy = 0; topPanel.add(lbPhim, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(cbPhim, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; topPanel.add(lbPhong, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(cbPhong, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; topPanel.add(lbSuat, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(cbSuatChieu, gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;        
        topPanel.add(btnChonGhe, gbc);
        
        return topPanel;
    }
    
    
// ================ HAM TAO PANEL CENTER ==================   
    private JPanel createCenterPanel() {     
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        
        // CAC LABEL
        JLabel h1 = new JLabel("Sản phẩm");
        h1.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel h2 = new JLabel("Đơn giá (VND)", JLabel.CENTER);
        h2.setFont(new Font("Arial", Font.BOLD, 14));
        
        JLabel h3 = new JLabel("Số lượng", JLabel.CENTER);
        h3.setFont(new Font("Arial", Font.BOLD, 14));
        
        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setBackground(Color.WHITE);
        
        header.add(h1);
        header.add(h2);
        header.add(h3);
        
        // ============ PHAN LUA CHON SAN PHAM ==============
        JPanel rightPanelWrapper = new JPanel(new BorderLayout());
        rightPanelWrapper.setBackground(Color.WHITE);
        rightPanelWrapper.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Lựa chọn sản phẩm",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(120, 0, 0)
        ), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        List<SanPham> products = new SanPhamDAO().getAll();
        
        for (SanPham sp : products) {
            JPanel productPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            productPanel.setPreferredSize(new Dimension(10, 30)); // Fixed: h30
            productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            productPanel.setBackground(Color.WHITE);
                  
            JLabel lb = new JLabel(sp.getTenSanPham());
            lb.setFont(new Font("Arial", Font.PLAIN, 14));
            
            SpinnerNumberModel model = new SpinnerNumberModel(0, 0, null, 1);
            JSpinner sl = new JSpinner(model);
            ((JSpinner.DefaultEditor) sl.getEditor()).getTextField().setHorizontalAlignment(JTextField.CENTER);
            sl.setPreferredSize(new Dimension(60, 25));
            
            JLabel dg = new JLabel(formatMoney(sp.getDonGia())); 
            dg.setHorizontalAlignment(JLabel.CENTER); 
            dg.setFont(new Font("Arial", Font.PLAIN, 14));
            productPanel.add(lb);
            productPanel.add(dg);
            productPanel.add(sl);
            productPanel.putClientProperty("product", sp.getMaSanPham());
            listPanel.add(productPanel);
        }
        
        // them listpanel vao scroolpanel
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll.setBorder(null);       
        
        // them header va scrool vao rightPanelWrapper
        rightPanelWrapper.add(header, BorderLayout.NORTH);
        rightPanelWrapper.add(scroll, BorderLayout.CENTER);

        // ============= PHAN THONG TIN VE =================
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBackground(Color.WHITE);
        gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 15, 10, 15);
        gbc.anchor = GridBagConstraints.WEST;

        Font labelFont = new Font("Arial", Font.BOLD, 15);
        Font valueFont = new Font("Arial", Font.PLAIN, 15);
        Color labelColor = new Color(50, 50, 50);

        JLabel lb1 = new JLabel("Tên phim:");
        lb1.setFont(labelFont);
        lb1.setForeground(labelColor);
        lbTenPhim = new JLabel("Chưa chọn phim");
        lbTenPhim.setFont(valueFont);

        JLabel lb2 = new JLabel("Tên phòng:");
        lb2.setFont(labelFont);
        lb2.setForeground(labelColor);
        lbTenPhong = new JLabel("Chưa chọn phòng");
        lbTenPhong.setFont(valueFont);

        JLabel lb3 = new JLabel("Thời lượng:");
        lb3.setFont(labelFont);
        lb3.setForeground(labelColor);
        lbThoiLuong = new JLabel("-");
        lbThoiLuong.setFont(valueFont);

        JLabel lb4 = new JLabel("Thể loại:");
        lb4.setFont(labelFont);
        lb4.setForeground(labelColor);
        lbTheLoai = new JLabel("-");
        lbTheLoai.setFont(valueFont);

        JLabel lb5 = new JLabel("Thời gian bắt đầu:");
        lb5.setFont(labelFont);
        lb5.setForeground(labelColor);
        lbThoiGianBD = new JLabel("-");
        lbThoiGianBD.setFont(valueFont);

        JLabel lb6 = new JLabel("Ghế đã chọn:");
        lb6.setFont(labelFont);
        lb6.setForeground(labelColor);
        
        taGheDaChon = new JTextArea("-");
        taGheDaChon.setFont(valueFont);
        taGheDaChon.setEditable(false);
        taGheDaChon.setFocusable(false);
        taGheDaChon.setOpaque(true);
        taGheDaChon.setBackground(Color.WHITE); 
        taGheDaChon.setLineWrap(true);
        taGheDaChon.setWrapStyleWord(true);
        JScrollPane scrollSelected = new JScrollPane(
                taGheDaChon,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollSelected.setBorder(null);
        scrollSelected.setPreferredSize(new Dimension(240, 18));
        
        JLabel lb7 = new JLabel("Giá vé:");
        lb7.setFont(labelFont); 
        lb7.setForeground(labelColor); 
        lbGiaVe = new JLabel("-");
        lbGiaVe.setFont(valueFont); 
        
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(Color.WHITE);

        gbc.gridx = 0; gbc.gridy = 0; infoPanel.add(lb1, gbc);
        gbc.gridx = 1; infoPanel.add(lbTenPhim, gbc);

        gbc.gridx = 0; gbc.gridy = 1; infoPanel.add(lb2, gbc);
        gbc.gridx = 1; infoPanel.add(lbTenPhong, gbc);

        gbc.gridx = 0; gbc.gridy = 2; infoPanel.add(lb3, gbc);
        gbc.gridx = 1; infoPanel.add(lbThoiLuong, gbc);

        gbc.gridx = 0; gbc.gridy = 3; infoPanel.add(lb4, gbc);
        gbc.gridx = 1; infoPanel.add(lbTheLoai, gbc);

        gbc.gridx = 0; gbc.gridy = 4; infoPanel.add(lb5, gbc);
        gbc.gridx = 1; infoPanel.add(lbThoiGianBD, gbc);

        gbc.gridx = 0; gbc.gridy = 5; infoPanel.add(lb6, gbc);
        gbc.gridx = 1; infoPanel.add(scrollSelected, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; infoPanel.add(lb7, gbc);
        gbc.gridx = 1; infoPanel.add(lbGiaVe, gbc);        
        
        // titleborder boc ngoai infopanel (right-panel)
        JPanel leftPanelWrapper = new JPanel(new BorderLayout());
        leftPanelWrapper.setBackground(Color.WHITE);
        leftPanelWrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Thông tin vé",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(120, 0, 0)
        ));        
        
        leftPanelWrapper.add(infoPanel, BorderLayout.CENTER);
        
        centerPanel.add(leftPanelWrapper);
        centerPanel.add(rightPanelWrapper);
        
        return centerPanel;
    }
 
// ================== HAM TAO BOTTOM PANEL =================    
    private JPanel createBottomPanel() {
        btnLamMoi = makeButton(100, 36, Color.DARK_GRAY, "Làm mới");
        
        // NUT GIO HANG
        btnGioHang = makeButton(100, 36, new Color(80, 80, 80), "Giỏ hàng");
        
        // NUT XAC NHAN
        btnXacNhan = makeButton(160, 36, new Color(200, 0, 0), "Xác nhận thanh toán");

        // BOTTOM PANEL
        JPanel leftBottomPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 10));
        leftBottomPanel.setBackground(Color.WHITE);
        leftBottomPanel.add(btnLamMoi);
        
        JPanel rightBottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        rightBottomPanel.setBackground(Color.WHITE);
        rightBottomPanel.add(btnGioHang);
        rightBottomPanel.add(btnXacNhan);
        
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBackground(Color.WHITE); 
        bottomPanel.setPreferredSize(new Dimension(0, 60));
        bottomPanel.add(leftBottomPanel, BorderLayout.WEST);
        bottomPanel.add(rightBottomPanel, BorderLayout.EAST);
        
        return bottomPanel;
    }
    
// ========== HAM TAO CAC THE CARD CHO GIO HANG ============
    private JPanel createVeCard(String seatcode) {
        JPanel card = new JPanel();
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBackground(Color.DARK_GRAY);
        card.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(255, 40, 40)),
                BorderFactory.createEmptyBorder(12, 12, 12, 12)
        ));

        JLabel lbtphim = new JLabel(lbTenPhim.getText());
        lbtphim.setFont(new Font("Segoe UI", Font.BOLD, 17));
        lbtphim.setForeground(new Color(255, 70, 70));
        lbtphim.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lb_phong = new JLabel("Tên phòng: " + lbTenPhong.getText());
        JLabel lb_suat = new JLabel("Suất chiếu: " + lbThoiGianBD.getText());
        JLabel lb_ghe = new JLabel("Ghế: " + seatcode);
        JLabel lb_gia = new JLabel("Giá vé: " + lbGiaVe.getText());

        lb_phong.setForeground(Color.WHITE);
        lb_suat.setForeground(Color.WHITE);
        lb_ghe.setForeground(Color.WHITE);
        lb_gia.setForeground(new Color(255, 80, 80));

        Font small = new Font("Segoe UI", Font.PLAIN, 14);
        lb_phong.setFont(small);
        lb_suat.setFont(small);
        lb_ghe.setFont(small);
        lb_gia.setFont(new Font("Segoe UI", Font.BOLD, 15));

        lb_phong.setAlignmentX(Component.LEFT_ALIGNMENT);
        lb_suat.setAlignmentX(Component.LEFT_ALIGNMENT);
        lb_ghe.setAlignmentX(Component.LEFT_ALIGNMENT);
        lb_gia.setAlignmentX(Component.LEFT_ALIGNMENT);

        card.add(lbtphim);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        card.add(lb_phong);
        card.add(Box.createRigidArea(new Dimension(0, 3)));

        card.add(lb_suat);
        card.add(Box.createRigidArea(new Dimension(0, 3)));

        card.add(lb_ghe);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        JSeparator sep = new JSeparator();
        sep.setForeground(new Color(255, 40, 40));
        sep.setMaximumSize(new Dimension(Integer.MAX_VALUE, 2));
        card.add(sep);
        card.add(Box.createRigidArea(new Dimension(0, 8)));

        card.add(lb_gia);
        return card;
    }
    
// ================ HAM LAM MOI DU LIEU ================
    private void clearForm() {
        this.maPhimDaChon = this.maPhongChieuDaChon = this.maSuatChieuDaChon;
        taGheDaChon.setText("-");
        cbPhim.setSelectedItem(null); 
        cbPhong.setSelectedItem(null);
        
        for (Component c : listPanel.getComponents()) {
            if (c instanceof JPanel productPanel) {        
                JSpinner sl = (JSpinner) productPanel.getComponent(2);
                sl.setValue(0); 
            }
        }       
    }
    
    private JButton makeButton(int w, int h, Color bg, String value) {
        JButton btn = new JButton(value);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setMaximumSize(new Dimension(w, h));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
    
// =============== HAM DINH DANG TIENG VIET =================
    private String formatMoney(BigDecimal amount) {
        if (amount == null) return "0 đ";
        DecimalFormat df = new DecimalFormat("#,###");
        df.setRoundingMode(RoundingMode.DOWN);
        return df.format(amount) + " đ";
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm("Trương Tuấn Tú", "Quản lý", 2);
            mainForm.setVisible(true);
        });
    }   
}
