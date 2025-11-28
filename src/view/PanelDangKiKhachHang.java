package view;

import com.toedter.calendar.JDateChooser;
import javax.swing.*;
import java.awt.*;
import java.util.Date;
import model.KhachHang;
import dao.KhachHangDAO;

public class PanelDangKiKhachHang extends JDialog {

    private JTextField txtTen;
    private JRadioButton rdoNam, rdoNu, rdoKhac;
    private JTextField txtSoDienThoai;
    private JTextField txtEmail;
    private JTextField txtHangThanhVien;
    private JTextField txtDiemTichLuy;
    private JDateChooser dcNgaySinh;
    

    private JButton btnHuy;
    private JButton btnXacNhan;

        // ====== INTERFACE CALLBACK ======
    public interface SignUpListener {
        void onSignUpDone(int maKH);
    }
    
    public PanelDangKiKhachHang(JDialog parent, SignUpListener listener) {
        super(parent, "Đăng Ký Thành Viên", true);
        initUI();
        setSize(450, 500);
        setLocationRelativeTo(parent);
        
        btnXacNhan.addActionListener(e -> {
            KhachHang kh = getKhachHang();
            if (kh == null) {
                msg("Vui lòng điền đầy đủ thông tin!", 
                        "Lỗi", JOptionPane.ERROR_MESSAGE);
                return;
            }
            try {
                int maKH = new KhachHangDAO().insertKhachHangAndReturnId(kh);
                listener.onSignUpDone(maKH); 
            } catch (Exception ex) {
                System.getLogger(PanelDangKiKhachHang.class.getName()).log(System.Logger.Level.ERROR, (String) null, ex);
            }
            msg("Đăng kí thành công!", "Thành công", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        });
    }

    private void initUI() {
        JPanel content = new JPanel(new BorderLayout());
        content.setBackground(Color.WHITE);

        // Panel chứa form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Color.WHITE);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.anchor = GridBagConstraints.WEST;

        JLabel title = new JLabel("ĐĂNG KÝ THÀNH VIÊN");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(new Color(180 , 0, 0));
        title.setHorizontalAlignment(SwingConstants.CENTER);

        content.add(title, BorderLayout.NORTH);

        // ======= Tạo các thành phần =======

        txtTen = new JTextField(20);
        txtSoDienThoai = new JTextField(20);
        txtEmail = new JTextField(20);
        txtDiemTichLuy = new JTextField(20);
        txtHangThanhVien = new JTextField(20);
        
        txtDiemTichLuy.setText("0");  
        txtDiemTichLuy.setEnabled(false); 
        txtHangThanhVien.setText("Sắt"); 
        txtHangThanhVien.setEnabled(false); 
        
        dcNgaySinh = new com.toedter.calendar.JDateChooser("dd/MM/yyyy", "##/##/####", '_'); 

        // Radio giới tính
        rdoNam = new JRadioButton("Nam");
        rdoNu = new JRadioButton("Nữ");
        rdoKhac = new JRadioButton("Khác");
        rdoKhac.setSelected(true); 

        ButtonGroup group = new ButtonGroup(); 
        group.add(rdoNam);
        group.add(rdoNu);
        group.add(rdoKhac);

        rdoNam.setBackground(Color.WHITE);
        rdoNu.setBackground(Color.WHITE);
        rdoKhac.setBackground(Color.WHITE);

        // ======= Add vào form =======
        addRow(form, gbc, 0, "Tên khách hàng:", txtTen);
        addRow(form, gbc, 1, "Ngày sinh:", dcNgaySinh);

        // Giới tính
        gbc.gridx = 0;
        gbc.gridy = 2;
        form.add(new JLabel("Giới tính:"), gbc);

        JPanel genderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        genderPanel.setBackground(Color.WHITE);
        genderPanel.add(rdoNam);
        genderPanel.add(rdoNu);
        genderPanel.add(rdoKhac);

        gbc.gridx = 1;
        form.add(genderPanel, gbc);

        addRow(form, gbc, 3, "Số điện thoại:", txtSoDienThoai);
        addRow(form, gbc, 4, "Email:", txtEmail);
        addRow(form, gbc, 5, "Hạng thành viên:", txtHangThanhVien);
        addRow(form, gbc, 6, "Điểm tích lũy:", txtDiemTichLuy);

        content.add(form, BorderLayout.CENTER);

        // ======= Buttons =======
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(Color.WHITE);

        btnHuy = new JButton("Hủy");
        btnXacNhan = new JButton("Xác Nhận");

        styleButton(btnHuy, Color.DARK_GRAY);
        styleButton(btnXacNhan, new Color(180, 0, 0));
        
        btnHuy.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnHuy.setBackground(new Color(110, 110, 110));
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                btnHuy.setBackground(Color.DARK_GRAY);
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
        
        buttonPanel.add(btnHuy);
        buttonPanel.add(btnXacNhan);
        
        
        content.add(buttonPanel, BorderLayout.SOUTH);

        add(content);
 
    }

    private void styleButton(JButton btn, Color background) {
        btn.setBackground(background);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setPreferredSize(new Dimension(110, 35));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    private void addRow(JPanel panel, GridBagConstraints gbc, int row, String label, JComponent component) {
        gbc.gridx = 0;
        gbc.gridy = row;
        panel.add(new JLabel(label), gbc);

        gbc.gridx = 1;
        panel.add(component, gbc);
    }
    
    private KhachHang getKhachHang() {
        String ten = txtTen.getText().trim();
        String sdt = txtSoDienThoai.getText().trim();
        String email = txtEmail.getText().trim();
        Date ns = (Date) dcNgaySinh.getDate();
        
        if(ten.isEmpty() || sdt.isEmpty() || email.isEmpty() || ns == null) return null;
        
        int diem = Integer.parseInt(txtDiemTichLuy.getText().trim());
        String hang = txtHangThanhVien.getText().trim();

        String gt;
        if (rdoNam.isSelected()) {
            gt = "Nam";
        } else if (rdoNu.isSelected()) {
            gt = "Nữ";
        } else {
            gt = "Khác";
        }

        return new KhachHang(0, ten, ns, gt, sdt, email, hang, diem); 
    }
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
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                btnOK.setBackground(new Color(220, 0, 0));
            }
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

