package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class PanelBanHang extends JPanel {

    private JComboBox<String> cbPhim, cbPhong, cbSuatChieu;
    private JLabel lbTenPhim, lbTenPhong, lbThoiLuong, lbTheLoai, lbThoiGianBD, lbGheDaChon;

    public PanelBanHang() {
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

        cbPhim = new JComboBox<>(new String[]{"-- Chọn phim --", "Avengers", "Doraemon", "Inception"});
        cbPhong = new JComboBox<>(new String[]{"-- Chọn phòng --", "Phòng 1", "Phòng 2"});
        cbSuatChieu = new JComboBox<>(new String[]{"-- Chọn suất --", "10:00", "14:00", "19:00"});

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
        JPanel header = new JPanel(new GridLayout(1, 2));
        header.setBackground(Color.WHITE);
        JLabel h1 = new JLabel("Sản phẩm");
        h1.setFont(new Font("Arial", Font.BOLD, 14));
        JLabel h2 = new JLabel("Số lượng", JLabel.CENTER);
        h2.setFont(new Font("Arial", Font.BOLD, 14));
        header.add(h1);
        header.add(h2);
        leftPanelWrapper.add(header, BorderLayout.NORTH);
        
        // listpanel: moi san pham la 1 panel, gom vao listpanel
        JPanel listPanel = new JPanel();
        listPanel.setLayout(new BoxLayout(listPanel, BoxLayout.Y_AXIS));
        listPanel.setBackground(Color.WHITE);

        // them listpanel vao scroolpanel
        JScrollPane scroll = new JScrollPane(listPanel);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scroll.setBorder(null);       
        
        String[] products = {
            "Bắp rang bơ",
            "Nước ngọt Cocacola",
            "siêu thị cũ",
            "Kẹo sữa mikita",
            "Tương ớt Chin-su",
            "con cho",
            "con meo",
            "crush",
            "banh mi Sai Gon",
            "con heo",
            "banh mi Ha Noi",
            "36"
        };
        
//        Map<String, JTextField> soLuongMap = new HashMap<>();  // dung sau

        for (String p : products) {
            JPanel productPanel = new JPanel(new GridLayout(1, 2, 10, 0));
            productPanel.setPreferredSize(new Dimension(10, 30)); // Fixed: h30
            productPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));
            productPanel.setBackground(Color.WHITE);
                  
            JLabel lb = new JLabel(p);
            lb.setFont(new Font("Arial", Font.PLAIN, 14));
            
            JTextField tf = new JTextField("0");
            tf.setHorizontalAlignment(JTextField.CENTER);
            tf.setPreferredSize(new Dimension(10, 10));
//            soLuongMap.put(p, tf);  // dung sau
            productPanel.add(lb);
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
        
        rightPanelWrapper.add(infoPanel, BorderLayout.CENTER);
        
        centerPanel.add(rightPanelWrapper);
        centerPanel.add(leftPanelWrapper);
        add(centerPanel, BorderLayout.CENTER);
        
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
            String phim = (String) cbPhim.getSelectedItem();
            if (phim != null && !phim.equals("-- Chọn phim --")) {
                lbTenPhim.setText(phim);
                lbThoiLuong.setText("120 phút");
                lbTheLoai.setText("Hành động");
            }
        });

        cbPhong.addActionListener(e -> {
            String phong = (String) cbPhong.getSelectedItem();
            if (phong != null && !phong.equals("-- Chọn phòng --"))
                lbTenPhong.setText(phong);
        });

        cbSuatChieu.addActionListener(e -> {
            String suat = (String) cbSuatChieu.getSelectedItem();
            if (suat != null && !suat.equals("-- Chọn suất --"))
                lbThoiGianBD.setText(suat);
        });

        btnChonGhe.addActionListener(e -> {
            PanelChonGhe dialog = new PanelChonGhe(
                (JFrame) SwingUtilities.getWindowAncestor(this),
                seats -> lbGheDaChon.setText(String.join(", ", seats))
            );
            dialog.setVisible(true);
        });
        
        btnGioHang.addActionListener(e -> openGioHangDialog()); 


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
    
    private void openGioHangDialog() {
        // Lấy Frame cha (nếu PanelBanHang nằm trong một JFrame)
        Frame parent = (Frame) SwingUtilities.getWindowAncestor(this);

        PanelGioHang dialog = new PanelGioHang(parent);

        // Set listener để nhận kết quả từ dialog
        dialog.setCartListener(new PanelGioHang.CartListener() {
            @Override
            public void onCartConfirmed() {
                System.out.println("Giỏ hàng được xác nhận!");
                // xử lý confirm ở đây (ví dụ: tạo hóa đơn)
            }

            @Override
            public void onCartCanceled() {
                System.out.println("Giỏ hàng bị hủy");
            }
        });

        // Thêm các sản phẩm đang có trong giỏ tại PanelBanHang
//        for (SanPham sp : danhSachSanPhamTrongGio) {
//            dialog.addItem(
//                sp.getTen(),
//                sp.getLoai(),
//                sp.getSoLuong(),
//                sp.getGia()
//            );
//        }

        dialog.setVisible(true);
    }
}
