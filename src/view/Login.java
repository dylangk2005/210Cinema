package view;

import dao.ChucVuDAO;
import dao.NhanVienDAO;
import dao.TaiKhoanDAO;
import model.ChucVu;
import model.NhanVien;
import model.TaiKhoan;

import javax.swing.*;
import java.awt.*;

public class Login extends JFrame {
    private JTextField txtUser;
    private JPasswordField txtPass;

    public Login() {
        initUI();
    }
    
    private void initUI() {
        setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/view/icons/meo210.png")));
        setTitle("LOGIN");
        setSize(700, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // === PANEL TRÁI - LOGO - ẢNH ===
        JPanel left = new JPanel();
        left.setBackground(new Color(139, 0, 0));
        left.setPreferredSize(new Dimension(300, 450));
        left.setLayout(new GridBagLayout());

        // Panel con để chứa ảnh + chữ 210CINEMA 
        JPanel logoContainer = new JPanel();
        logoContainer.setOpaque(false);
        logoContainer.setLayout(new BoxLayout(logoContainer, BoxLayout.X_AXIS));

        // Ảnh
        JLabel imgMeo = new JLabel();
        ImageIcon meoIcon = loadIcon("/view/icons/meo210.png", 80, 80); // chỉnh size tùy ý
        if (meoIcon != null) {
            imgMeo.setIcon(meoIcon);
        }
        logoContainer.add(imgMeo);
        logoContainer.add(Box.createHorizontalStrut(20)); // khoảng cách giữa mèo và chữ

        // Chữ 210CINEMA
        JLabel lbLogo = new JLabel("210CINEMA");
        lbLogo.setFont(new Font("Arial", Font.BOLD, 30));
        lbLogo.setForeground(Color.WHITE);
        logoContainer.add(lbLogo);

        // Đẩy toàn bộ logoContainer vào giữa panel trái
        left.add(logoContainer);
        

        // === PANEL PHẢI - FORM ===
        JPanel right = new JPanel(new GridBagLayout());
        right.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(12, 12, 12, 12);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Tiêu đề
        JLabel lbTitle = new JLabel("ĐĂNG NHẬP HỆ THỐNG", SwingConstants.CENTER);
        lbTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lbTitle.setForeground(new Color(139, 0, 0));
        gbc.gridx = 0; gbc.gridy = 0; gbc.gridwidth = 2;
        right.add(lbTitle, gbc);

        // User
        gbc.gridwidth = 1; gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        right.add(new JLabel("Tài khoản:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtUser = new JTextField(18);
        txtUser.setFont(new Font("Arial", Font.PLAIN, 16));
        right.add(txtUser, gbc);

        // Pass
        gbc.gridx = 0; gbc.gridy++;
        gbc.anchor = GridBagConstraints.EAST;
        right.add(new JLabel("Mật khẩu:"), gbc);

        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        txtPass = new JPasswordField(18);
        txtPass.setFont(new Font("Arial", Font.PLAIN, 16));
        right.add(txtPass, gbc);

        // Button
        gbc.gridx = 0; gbc.gridy++; gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JButton btnLogin = new JButton("ĐĂNG NHẬP");
        btnLogin.setFont(new Font("Arial", Font.BOLD, 16));
        btnLogin.setBackground(new Color(139, 0, 0));
        btnLogin.setForeground(Color.WHITE);
        btnLogin.setFocusPainted(false);
        btnLogin.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        right.add(btnLogin, gbc);

        // === SỰ KIỆN ===
        btnLogin.addActionListener(e -> dangNhap());

        // === GỘP LẠI ===
        add(left, BorderLayout.WEST);
        add(right, BorderLayout.CENTER);
    }
    
    // Đăng nhập
    private void dangNhap() {
        String username = txtUser.getText().trim();
        String password = new String(txtPass.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            thongBao("Vui lòng nhập đầy đủ thông tin!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        TaiKhoanDAO tkDAO = new TaiKhoanDAO();
        NhanVienDAO nvDAO = new NhanVienDAO();
        ChucVuDAO cvDAO = new ChucVuDAO();

        TaiKhoan tk = tkDAO.checkLogin(username, password);
        if (tk == null) {
            thongBao("Sai tài khoản hoặc mật khẩu!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        NhanVien nv = nvDAO.getByID(tk.getMaNhanVien());
        if (nv == null) {
            thongBao("Không tìm thấy nhân viên!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        ChucVu cv = cvDAO.getByIDChucVu(nv.getMaChucVu());
        if (cv == null) {
            thongBao("Không tìm thấy chức vụ!", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return;
        }

        thongBao("Đăng nhập thành công! Chào " + nv.getHoTenNhanVien(), "Thành công", JOptionPane.INFORMATION_MESSAGE);

        dispose(); // Đóng login

        // Mở MainFrom
        SwingUtilities.invokeLater(() -> {
            MainForm mainForm = new MainForm(nv.getHoTenNhanVien(), cv.getTenChucVu(), tk.getMaNhanVien());
            mainForm.setVisible(true);
        });
    }
    
    private ImageIcon loadIcon(String path, int w, int h) {
        java.net.URL imgURL = getClass().getResource(path);
        if (imgURL != null) {
            ImageIcon icon = new ImageIcon(imgURL);
            Image img = icon.getImage().getScaledInstance(w, h, Image.SCALE_SMOOTH);
            return new ImageIcon(img);
        }
        System.err.println("Không tìm thấy icon: " + path);
        return null;
    }
        
    private void thongBao(String msg, String title, int type) {
        // Tạo nút OK 
        JButton btnOK = new JButton("OK");
        btnOK.setBackground(new Color(139, 0, 0));
        btnOK.setForeground(Color.WHITE);
        btnOK.setFont(new Font("SegoeUI", Font.BOLD, 15));
        btnOK.setFocusPainted(false);
        btnOK.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btnOK.setPreferredSize(new Dimension(110, 30));
        btnOK.setBorder(BorderFactory.createEmptyBorder(10, 30, 10, 30));
        
        // Hover 
        btnOK.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOK.setBackground(new Color(200, 0, 0));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnOK.setBackground(new Color(139, 0, 0));
            }
        });

        // Hiển thị dialog chỉ có mỗi nút ok
        JOptionPane optionPane = new JOptionPane(
            msg,
            type,
            JOptionPane.DEFAULT_OPTION,
            null,
            new Object[]{btnOK},
            btnOK
        );

        JDialog dialog = optionPane.createDialog(this, title);
        dialog.setResizable(false);
        btnOK.addActionListener(e -> dialog.dispose());  // nhấn là đóng
        dialog.setVisible(true);
    }
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Login().setVisible(true));
    }
}