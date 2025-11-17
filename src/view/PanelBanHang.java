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
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1, true),
                "THÔNG TIN VÉ",
                0, 0,
                new Font("Arial", Font.BOLD, 16),
                new Color(150, 0, 0)
        ));
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

        add(infoPanel, BorderLayout.CENTER);

        // ====== Nút xác nhận ======
        JButton btnXacNhan = new JButton("XÁC NHẬN ĐẶT VÉ");
        btnXacNhan.setBackground(new Color(200, 0, 0));
        btnXacNhan.setForeground(Color.WHITE);
        btnXacNhan.setFont(new Font("Arial", Font.BOLD, 14));

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 20, 10));
        bottomPanel.setBackground(Color.WHITE);
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


        btnXacNhan.addActionListener(e -> {
            JOptionPane.showMessageDialog(this,
                    "Đặt vé thành công!\nPhim: " + cbPhim.getSelectedItem() +
                            "\nPhòng: " + cbPhong.getSelectedItem() +
                            "\nSuất: " + cbSuatChieu.getSelectedItem(),
                    "Xác nhận", JOptionPane.INFORMATION_MESSAGE);
        });
    }
}
