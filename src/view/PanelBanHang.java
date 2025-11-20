package view;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import dao.*;
import model.*;

public class PanelBanHang extends JPanel {

    private JComboBox<Phim> cbPhim;
    private JComboBox<PhongChieu> cbPhong;
    private JComboBox<SuatChieu> cbSuatChieu;
    private JLabel lbTenPhim, lbTenPhong, lbThoiLuong, lbTheLoai, lbThoiGianBD, lbGheDaChon, lbGiaVe;
    private int maPhongChieuDaChon, maPhimDaChon, maSuatChieuDaChon;
    private List<Phim> danhSachPhim;
    private List<PhongChieu> danhSachPhongChieu;
    private List<SuatChieu> danhSachSuatChieu;

    public PanelBanHang() {
        maPhongChieuDaChon = maPhimDaChon = maSuatChieuDaChon = -1;
        setLayout(new BorderLayout(15, 15));
        setBackground(Color.WHITE);

        // ====== PHẦN TRÊN: chọn phim, phòng, suất chiếu ======
        JPanel topPanel = new JPanel(new GridBagLayout());
        topPanel.setBorder(BorderFactory.createTitledBorder("CHỌN PHIM VÀ SUẤT CHIẾU"));
        topPanel.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lbPhim = new JLabel("Phim:");
        JLabel lbPhong = new JLabel("Phòng:");
        JLabel lbSuat = new JLabel("Suất chiếu:");
        
        danhSachPhim = new PhimDAO().getAllPhim();
        
        cbPhim = new JComboBox<>();
        cbPhim.addItem(null);
        
        for (Phim p : danhSachPhim) {
            cbPhim.addItem(p);
        }
        
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
        
        danhSachPhongChieu = new PhongChieuDAO().getAllPhongChieu();
        cbPhong = new JComboBox<>();
        cbPhong.addItem(null);
        
        for (PhongChieu p : danhSachPhongChieu) {
            cbPhong.addItem(p);
        }       
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
        
        cbSuatChieu = new JComboBox<>();
        cbSuatChieu.addItem(null);
        
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

        JButton btnChonGhe = new JButton("CHỌN GHẾ");
        btnChonGhe.setBackground(new Color(200, 0, 0));
        btnChonGhe.setForeground(Color.WHITE);
        btnChonGhe.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0; gbc.gridy = 0; topPanel.add(lbPhim, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(cbPhim, gbc);
        gbc.gridx = 0; gbc.gridy = 1; gbc.gridwidth = 1; topPanel.add(lbPhong, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(cbPhong, gbc);
        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 1; topPanel.add(lbSuat, gbc);
        gbc.gridx = 1; gbc.gridwidth = 2; topPanel.add(cbSuatChieu, gbc);

        gbc.gridx = 3; gbc.gridy = 1; gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.NONE; gbc.anchor = GridBagConstraints.CENTER;
        topPanel.add(btnChonGhe, gbc);

        add(topPanel, BorderLayout.NORTH);

        // ====== PHẦN DƯỚI: Thông tin vé ======
        // Khoi boc ngoai
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 20, 0));
        centerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        centerPanel.setBackground(Color.WHITE);
        
        // titleborder boc ngoai listpanel (left-panel)
        JPanel leftPanelWrapper = new JPanel(new BorderLayout());
        leftPanelWrapper.setBackground(Color.WHITE);
        leftPanelWrapper.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Lựa chọn sản phẩm",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(120, 0, 0)
        ), BorderFactory.createEmptyBorder(20, 20, 20, 20)));
        
        // panel header: tieu de cot
        JPanel header = new JPanel(new GridLayout(1, 3));
        header.setBackground(Color.WHITE);
        JLabel h1 = new JLabel("Sản phẩm");
        h1.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel h2 = new JLabel("Đơn giá (VND)", JLabel.CENTER);
        h2.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel h3 = new JLabel("Số lượng", JLabel.CENTER);
        h3.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(h1);
        header.add(h2);
        header.add(h3);
        leftPanelWrapper.add(header, BorderLayout.NORTH);
        
        // listpanel: moi san pham la 1 panel, gom vao listpanel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        // them listpanel vao scroolpanel
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(null);       
        
        List<SanPham> products = new SanPhamDAO().getAllSanPham();
        
//        Map<String, JTextField> soLuongMap = new HashMap<>();  // dung sau

        for (SanPham sp : products) {
            JPanel productPanel = new JPanel(new GridLayout(1, 3, 10, 0));
            productPanel.setPreferredSize(new Dimension(10, 30)); // Fixed: h30
            productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            productPanel.setBackground(Color.WHITE);
                  
            JLabel lb = new JLabel(sp.getTenSanPham());
            lb.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JTextField tf = new JTextField("0");
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setPreferredSize(new Dimension(10, 10));
            
            JLabel dg = new JLabel(sp.getDonGia().toPlainString());
            dg.setHorizontalAlignment(JLabel.CENTER); 
            dg.setFont(new Font("Arial", Font.PLAIN, 14));
//            soLuongMap.put(p, tf);  // dung sau
            productPanel.add(lb);
            productPanel.add(dg);
            productPanel.add(tf);
            listPanel.add(productPanel);
        }
        leftPanelWrapper.add(scroll, BorderLayout.CENTER);

        // titleborder boc ngoai infopanel (right-panel)
        JPanel rightPanelWrapper = new JPanel(new BorderLayout());
        rightPanelWrapper.setBackground(Color.WHITE);
        rightPanelWrapper.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "Thông tin vé",
                0, 0,
                new Font("Arial", Font.BOLD, 14),
                new Color(120, 0, 0)
        ));        
        
        // infopanel
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
        lbGheDaChon = new JLabel("Chưa chọn ghế");
        lbGheDaChon.setFont(valueFont);
        
        JLabel lb7 = new JLabel("Giá vé:");
        lb7.setFont(labelFont); 
        lb7.setForeground(labelColor); 
        lbGiaVe = new JLabel("-");
        lbGiaVe.setFont(valueFont); 

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
        gbc.gridx = 1; infoPanel.add(lbGheDaChon, gbc);
        
        gbc.gridx = 0; gbc.gridy = 6; infoPanel.add(lb7, gbc);
        gbc.gridx = 1; infoPanel.add(lbGiaVe, gbc);        
        
        rightPanelWrapper.add(infoPanel, BorderLayout.CENTER);
        
        centerPanel.add(rightPanelWrapper);
        centerPanel.add(leftPanelWrapper);
        add(centerPanel, BorderLayout.CENTER);
        
        // nut gio hang
        JButton btnGioHang = new JButton("GIỎ HÀNG");
        ImageIcon logoIcon = loadIcon("/view/icons/cart-shopping.png", 28, 28);
        if (logoIcon != null) {
            btnGioHang.setIcon(logoIcon);
        }
        btnGioHang.setPreferredSize(new Dimension(120, 36));
        btnGioHang.setMaximumSize(new Dimension(120, 36));
        btnGioHang.setMargin(new Insets(0, 2, 0, 2));
        btnGioHang.setBackground(new Color(200, 0, 0));
        btnGioHang.setForeground(Color.WHITE);
        btnGioHang.setFont(new Font("Arial", Font.BOLD, 13));
        
        // ====== Nút xác nhận ======
        JButton btnXacNhan = new JButton("XÁC NHẬN ĐẶT VÉ");
        btnXacNhan.setPreferredSize(new Dimension(160, 36));
        btnXacNhan.setMaximumSize(new Dimension(160, 36));
        btnXacNhan.setBackground(new Color(200, 0, 0));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 13));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(btnGioHang);
        bottomPanel.add(btnXacNhan);
        add(bottomPanel, BorderLayout.SOUTH);

        // ====== SỰ KIỆN ======
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
                danhSachSuatChieu = new SuatChieuDAO().getSuatChieuByPhimAndPhong(maPhimDaChon, maPhongChieuDaChon);
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
                danhSachSuatChieu = new SuatChieuDAO().getSuatChieuByPhimAndPhong(maPhimDaChon, maPhongChieuDaChon);
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
                lbGiaVe.setText(p.getGiaVeCoBan().toString() + " vnđ / ghế");
                maSuatChieuDaChon = p.getMaSuatChieu();
            } else {
                lbThoiGianBD.setText("-");
                lbGiaVe.setText("-"); 
                maSuatChieuDaChon = -1;
            }
            
        });

        btnChonGhe.addActionListener(e -> {
            PanelChonGhe dialog = new PanelChonGhe(
                maPhongChieuDaChon,    
                maSuatChieuDaChon,    
                (JFrame) SwingUtilities.getWindowAncestor(this),
                seats -> lbGheDaChon.setText(String.join(", ", seats))
            );
            dialog.setVisible(true);
        });
        
        btnGioHang.addActionListener(e -> openGioHangDialog(listPanel)); 

        btnXacNhan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công!\nPhim: " + cbPhim.getSelectedItem() +
                            "\nPhòng: " + cbPhong.getSelectedItem() +
                            "\nSuất: " + cbSuatChieu.getSelectedItem(),
                    "Xác nhận", JOptionPane.INFORMATION_MESSAGE);
        });
    }
    
    // Hàm load icon
    private ImageIcon loadIcon(String path, int w, int h) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        System.err.println("Icon không tìm thấy: " + path);
        return null;
    }
    
    private void openGioHangDialog(JPanel listPanel) {
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

        // Lấy thông tin vé
        String tenPhim = lbTenPhim.getText();
        String tenPhong = lbTenPhong.getText();
        String thoiLuong = lbThoiLuong.getText();
        String theLoai = lbTheLoai.getText();
        String thoiGianBD = lbThoiGianBD.getText();
        String gheDaChon = lbGheDaChon.getText();

        // Lấy danh sách sản phẩm
        List<GioHang> items = new ArrayList<>();
        for (Component c : listPanel.getComponents()) {
            if (c instanceof JPanel panel) {
                JLabel lb = (JLabel) panel.getComponent(0);
                JTextField tf = (JTextField) panel.getComponent(1);
                int soLuong = 0;
                try { soLuong = Integer.parseInt(tf.getText()); } catch (Exception ignored) {}
                if (soLuong > 0) {
                    items.add(new GioHang(lb.getText(), soLuong, 0));
                }
            }
        }

        PanelGioHang dialog = new PanelGioHang(parent,
                tenPhim, tenPhong, thoiLuong, theLoai, thoiGianBD, gheDaChon, items);

        dialog.setCartListener(new PanelGioHang.CartListener() {
            @Override
            public void onCartConfirmed() {
                System.out.println("Đã xác nhận giỏ hàng!");
            }

            @Override
            public void onCartCanceled() {
                System.out.println("Hủy giỏ hàng!");
            }
        });

        dialog.setVisible(true);
    }
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm("Trương Tuấn Tú", "Quản lý");
            mainForm.setVisible(true);
        });
    }
    
}
