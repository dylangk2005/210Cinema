package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.border.EmptyBorder;

public class MainForm extends JFrame {
    private JPanel mainPanel;
    private CardLayout card;
    private String chucVu; // Lưu chức vụ

    public MainForm(String hoTen, String chucVu) {
        this.chucVu = chucVu;
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/view/icons/meo210.png")));
        // cửa sổ chính
        setTitle("210CINEMA - Quản Lý Rạp Phim");
        setSize(1400, 800);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // menu trái
        JPanel menu = createSidebar(hoTen, chucVu);

        // main panel
        mainPanel = new JPanel();
        card = new CardLayout();
        mainPanel.setLayout(card);

        // Thêm panel vào main
        mainPanel.add(new PanelBanHang(), "banhang");
        mainPanel.add(new PanelPhim(), "phim");
        mainPanel.add(new PanelSuatChieu(), "suatchieu");
        mainPanel.add(new PanelSanPham(), "sanpham");
        mainPanel.add(new PanelKhachHang(), "khachhang");
        mainPanel.add(new PanelNhanVien(), "nhanvien");
        mainPanel.add(new PanelThongKe(), "thongke");
        mainPanel.add(new PanelPhongChieu(), "phongchieu");

        // Trang mặc định
        card.show(mainPanel, "banhang");

        // gộp
        add(menu, BorderLayout.WEST);
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // Side bar
    private JPanel createSidebar(String hoTen, String chucVu) {
        JPanel menu = new JPanel(new BorderLayout());
        menu.setPreferredSize(new Dimension(250, 800));
        menu.setBackground(Color.BLACK);

        // logo
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.X_AXIS));
        headerPanel.setPreferredSize(new Dimension(250, 100));
        headerPanel.setBackground(new Color(139, 0, 0));
        headerPanel.setBorder(new EmptyBorder(0, 18, 0, 0)); // lề trái nhẹ nhàng

        // Icon mèo
        JLabel iconLabel = new JLabel();
        ImageIcon logoIcon = loadIcon("/view/icons/meo210.png", 60, 60);
        if (logoIcon != null) {
            iconLabel.setIcon(logoIcon);
        }
        headerPanel.add(iconLabel);
        headerPanel.add(Box.createHorizontalStrut(12)); // khoảng cách icon ↔ chữ

        // Chữ 210CINEMA - đẩy sát trái hơn
        JLabel textLabel = new JLabel("210CINEMA");
        textLabel.setFont(new Font("Arial", Font.BOLD, 22));
        textLabel.setForeground(Color.WHITE);
        headerPanel.add(textLabel);

        // Đẩy hết nội dung sang trái 
        headerPanel.add(Box.createHorizontalGlue());
        menu.add(headerPanel, BorderLayout.NORTH);

        // user info
        JPanel userInfo = new JPanel(new GridLayout(2, 1, 5, 5));
        userInfo.setBackground(Color.BLACK);
        userInfo.setBorder(new EmptyBorder(20, 15, 20, 15));

        JLabel lbRole = new JLabel(chucVu.equals("Quản lý") ? "Quản lý" : "Nhân viên bán vé", JLabel.CENTER);
        lbRole.setForeground(Color.LIGHT_GRAY);
        lbRole.setFont(new Font("Arial", Font.PLAIN, 14));

        JLabel lbName = new JLabel(hoTen, JLabel.CENTER);
        lbName.setForeground(Color.WHITE);
        lbName.setFont(new Font("Arial", Font.BOLD, 16));

        userInfo.add(lbRole);
        userInfo.add(lbName);

        // buttons
        JPanel menuButtons = new JPanel();
        menuButtons.setLayout(new GridLayout(10, 1, 0, 8));
        menuButtons.setBackground(Color.BLACK);
        menuButtons.setBorder(new EmptyBorder(10, 15, 10, 15));

        // Danh sách nút
        String[][] buttons = {
            {"Bán Hàng", "ticket.png", "banhang"},
            {"Phim", "film.png", "phim"},
            {"Phòng Chiếu", "room.png", "phongchieu"},
            {"Suất Chiếu", "calendar.png", "suatchieu"},
            {"Sản Phẩm", "popcorn.png", "sanpham"},
            {"Nhân Viên", "employee.png", "nhanvien"},
            {"Khách Hàng", "customer.png", "khachhang"},
            {"Thống Kê", "chart.png", "thongke"},
            {"Đăng Xuất", "logout.png", "logout"}
        };
        
        // Tạo nút từ ds
        for (String[] btn : buttons) {
//            if (btn[0].isEmpty()) {
//                menuButtons.add(Box.createVerticalStrut(20));
//                continue;
//            }

            // Nếu là nhân viên thì chỉ hiển thị nút đăng xuất
            if (chucVu.equals("Nhân viên bán vé") && !btn[0].equals("Đăng Xuất")) {
                continue;
            }

            JButton button = createMenuButton(btn[0], btn[1]);
            String cardName = btn[2];

            if ("logout".equals(cardName)) {
                button.addActionListener(e -> dangXuat());
            } else {
                button.addActionListener(e -> card.show(mainPanel, cardName));
            }

            menuButtons.add(button);
        }

        // Gộp user + menu buttons
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.BLACK);
        centerPanel.add(userInfo, BorderLayout.NORTH);
        centerPanel.add(menuButtons, BorderLayout.CENTER);

        menu.add(centerPanel, BorderLayout.CENTER);
        return menu;
    }
    
    // Hàm tạo nút menu
    private JButton createMenuButton(String text, String iconFile) {
        ImageIcon icon = loadIcon("/view/icons/" + iconFile, 24, 24);
        JButton btn = new JButton(text, icon);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Arial", Font.BOLD, 15));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(180, 0, 0));
        btn.setBorderPainted(false);
        btn.setHorizontalAlignment(SwingConstants.LEFT);
        btn.setIconTextGap(12);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setPreferredSize(new Dimension(220, 50));

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
    // Hàm log out
    private void dangXuat() {
        // Tạo các nút tùy chỉnh
        JButton btnDangXuat = new JButton("Đăng Xuất");
        btnDangXuat.setBackground(new Color(220, 0, 0));
        btnDangXuat.setForeground(Color.WHITE);
        btnDangXuat.setFocusPainted(false);
        btnDangXuat.setFont(new Font("Arial", Font.BOLD, 14));
        btnDangXuat.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JButton btnHuy = new JButton("Hủy");
        btnHuy.setBackground(new Color(70, 70, 70));
        btnHuy.setForeground(Color.WHITE);
        btnHuy.setFocusPainted(false);
        btnHuy.setFont(new Font("Arial", Font.BOLD, 14));
        btnHuy.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        // Tạo JOptionPane tùy chỉnh
        JOptionPane optionPane = new JOptionPane(
            "Bạn có chắc muốn đăng xuất không?",
            JOptionPane.QUESTION_MESSAGE,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[] { btnDangXuat, btnHuy }, // 2 nút
            btnHuy // nút mặc định
        );

        // Tạo dialog
        JDialog dialog = optionPane.createDialog(this, "Xác nhận đăng xuất");

        // Bắt sự kiện khi nhấn nút
        btnDangXuat.addActionListener(e -> {
            dialog.dispose();
            dispose();                    // đóng MainForm
            new Login().setVisible(true); // mở lại form Login
        });

        btnHuy.addActionListener(e -> dialog.dispose());

        // Hover 
        btnDangXuat.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnDangXuat.setBackground(new Color(200, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnDangXuat.setBackground(new Color(220, 0, 0));
            }
        });
        btnHuy.addMouseListener( new java.awt.event.MouseAdapter(){
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHuy.setBackground(new Color(100, 100, 100));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHuy.setBackground(new Color(70, 70, 70));
            }
        });

        dialog.setVisible(true);
    }
    
    public void showPanelKhachHang() {
        card.show(mainPanel, "khachhang");
    }
    
}