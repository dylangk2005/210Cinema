package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import java.util.ArrayList;
import model.PhongChieu;
import dao.PhongChieuDAO;

public class PanelPhongChieu extends JPanel {
    private JTable tblPhongChieu;
    private DefaultTableModel model;

    private JTextField txtMaPhong, txtTenPhong, txtTimKiem;
    private JComboBox<String> cboTrangThai, cboLoaiManHinh, cboHeThongAmThanh;
    private JComboBox<Integer> cboSoGhe;

    private JButton btnThem, btnSua, btnXoa, btnLamMoi, btnTimTheoMa, btnTimTheoTen, btnLuu;
    private int maPhongHienTai;
    
    private final Color RED = new Color(200, 0, 0);

    public PanelPhongChieu() {
        setLayout(new BorderLayout());
        initComponents();
        initEvents();
        loadDatabase();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        setBackground(Color.WHITE);
        String[] cols = {
                "Mã Phòng", "Tên Phòng", "Số Ghế", "Trạng Thái", "Loại Màn Hình", "Âm Thanh"
        };
        model = new DefaultTableModel(cols, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Không cho phép edit trực tiếp trên table
            }
        };
        tblPhongChieu = new JTable(model);
        tblPhongChieu.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        JScrollPane scroll = new JScrollPane(tblPhongChieu);
        JPanel pnlCenter = new JPanel(new BorderLayout());
        pnlCenter.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            "Danh sách phòng chiếu",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(120, 0, 0)
        )); 
        pnlCenter.add(scroll, BorderLayout.CENTER);
        pnlCenter.setBackground(Color.WHITE); 
        
        JLabel lblTitle = new JLabel("Quản Lý Phòng Chiếu", SwingConstants.CENTER);
        lblTitle.setFont(new Font("SansSerif", Font.BOLD, 26));
        lblTitle.setForeground(new Color(0, 80, 80));
        lblTitle.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));


        txtMaPhong = new JTextField("AUTO"); 
        txtMaPhong.setEnabled(false); 
        txtTenPhong = new JTextField();

        cboTrangThai = new JComboBox<>(new String[]{"Đang sử dụng", "Bảo trì", "Không sử dụng"});
        cboLoaiManHinh = new JComboBox<>(new String[]{"2D", "3D", "IMAX"});
        cboHeThongAmThanh = new JComboBox<>(new String[]{"Dolby", "Atmos", "Standard"});
        cboSoGhe = new JComboBox<>(new Integer[]{120, 90, 60, 48, 36});
        cboTrangThai.setBackground(Color.WHITE); 
        cboLoaiManHinh.setBackground(Color.WHITE); 
        cboHeThongAmThanh.setBackground(Color.WHITE); 

        JPanel pnlForm = new JPanel(new GridLayout(3, 4, 10, 10));
        pnlForm.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(Color.GRAY, 1, true),
            "Thông tin phòng chiếu",
            0, 0,
            new Font("Arial", Font.BOLD, 14),
            new Color(120, 0, 0)
        )); 
        pnlForm.setBackground(Color.WHITE); 
        pnlForm.add(new JLabel("Mã phòng:"));
        pnlForm.add(txtMaPhong);
        pnlForm.add(new JLabel("Tên phòng:"));
        pnlForm.add(txtTenPhong);

        pnlForm.add(new JLabel("Số ghế:"));
        pnlForm.add(cboSoGhe);
        pnlForm.add(new JLabel("Trạng thái:"));
        pnlForm.add(cboTrangThai);

        pnlForm.add(new JLabel("Loại màn hình:"));
        pnlForm.add(cboLoaiManHinh);
        pnlForm.add(new JLabel("Âm thanh:"));
        pnlForm.add(cboHeThongAmThanh);
        
        JPanel pnlTop = new JPanel();
        pnlTop.setLayout(new BorderLayout());
        pnlTop.add(lblTitle, BorderLayout.PAGE_START);
        pnlTop.add(pnlForm, BorderLayout.CENTER);
        pnlTop.setBackground(Color.WHITE); 


        // ======= BUTTONS =======
        JPanel pnlButton = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        JPanel pnlSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 10));
        pnlButton.setBackground(Color.WHITE); 
        pnlSearch.setBackground(Color.WHITE); 

        btnThem = new JButton("Thêm");
        btnSua = new JButton("Sửa");
        btnXoa = new JButton("Xóa");
        btnLamMoi = new JButton("Làm mới");
        btnLuu = new JButton("Lưu");
        btnTimTheoMa = new JButton("Tìm theo mã");
        btnTimTheoTen = new JButton("Tìm theo tên");
        txtTimKiem = new JTextField("");
        txtTimKiem.setPreferredSize(new Dimension(200, 25)); 
        
        styleButton(btnThem, Color.DARK_GRAY);
        styleButton(btnSua, new Color(80, 80, 80));
        styleButton(btnXoa, RED);
        styleButton(btnLamMoi, RED);
        styleButton(btnTimTheoMa, RED);
        styleButton(btnTimTheoTen, RED);
        styleButton(btnLuu, new Color(80, 80, 80));

        pnlSearch.add(txtTimKiem);
        pnlSearch.add(btnTimTheoMa);
        pnlSearch.add(btnTimTheoTen);
        
        pnlButton.add(btnThem);
        pnlButton.add(btnSua);
        pnlButton.add(btnXoa);
        pnlButton.add(btnLamMoi);
        pnlButton.add(btnLuu);
        
        JPanel pnlAction = new JPanel(new BorderLayout());
        pnlAction.add(pnlSearch, BorderLayout.EAST);
        pnlAction.add(pnlButton, BorderLayout.WEST);
        pnlAction.setBackground(Color.WHITE); 
        
        add(pnlTop, BorderLayout.NORTH);
        add(pnlCenter, BorderLayout.CENTER);
        add(pnlAction, BorderLayout.SOUTH);
    }

    private void initEvents() {
        btnThem.addActionListener(e -> {
            String ten = txtTenPhong.getText();
            String so = cboSoGhe.getSelectedItem().toString();
            String tt = cboTrangThai.getSelectedItem().toString();
            String mh = cboLoaiManHinh.getSelectedItem().toString();
            String at = cboHeThongAmThanh.getSelectedItem().toString();

            maPhongHienTai += 1;
            model.addRow(new Object[]{maPhongHienTai, ten, so, tt, mh, at});
        });

        // ======= SỬA PHÒNG =======
        btnSua.addActionListener(e -> {
            int row = tblPhongChieu.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn phòng cần sửa.");
                return;
            }
            
            model.setValueAt(txtTenPhong.getText(), row, 1);
            model.setValueAt(cboSoGhe.getSelectedItem().toString(), row, 2);
            model.setValueAt(cboTrangThai.getSelectedItem().toString(), row, 3);
            model.setValueAt(cboLoaiManHinh.getSelectedItem().toString(), row, 4);
            model.setValueAt(cboHeThongAmThanh.getSelectedItem().toString(), row, 5);
        });

        // ======= XÓA PHÒNG =======
        btnXoa.addActionListener(e -> {
            int row = tblPhongChieu.getSelectedRow();
            if (row == -1) {
                JOptionPane.showMessageDialog(this, "Chọn phòng cần xóa.");
                return;
            }
            model.removeRow(row);
        });

        // ======= LÀM MỚI =======
        btnLamMoi.addActionListener(e -> clearForm());

        // ======= TÌM KIẾM =======
        btnTimTheoMa.addActionListener(e -> {
            searchPhongChieuByID(); 
        });
        
        btnTimTheoTen.addActionListener(e -> {
            searchPhongChieuByName();
        });
   
        btnLuu.addActionListener(e -> {
            updateDatabase();
            JOptionPane.showMessageDialog(this, "Đã cập nhật dữ liệu!");
        }); 
        
        // ======= CLICK TABLE => LOAD DATA LÊN FORM =======
        tblPhongChieu.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int row = tblPhongChieu.getSelectedRow();
                if (row != -1) {
                    txtMaPhong.setText(model.getValueAt(row, 0).toString());
                    txtTenPhong.setText(model.getValueAt(row, 1).toString());
                    cboSoGhe.setSelectedItem(model.getValueAt(row, 2).toString());
                    cboTrangThai.setSelectedItem(model.getValueAt(row, 3).toString());
                    cboLoaiManHinh.setSelectedItem(model.getValueAt(row, 4).toString());
                    cboHeThongAmThanh.setSelectedItem(model.getValueAt(row, 5).toString());
                }
            }
        });
        
    }

    private void clearForm() {
        txtMaPhong.setText("AUTO"); 
        txtTenPhong.setText("");
        cboSoGhe.setSelectedIndex(0); 
        cboTrangThai.setSelectedIndex(0);
        cboLoaiManHinh.setSelectedIndex(0);
        cboHeThongAmThanh.setSelectedIndex(0);
    }
    
    private void loadDatabase() {
        PhongChieuDAO pcdao = new PhongChieuDAO();
        maPhongHienTai = pcdao.getCurrentPhongChieuID();
        model.setRowCount(0);
        List<PhongChieu> oldList = pcdao.getAllPhongChieu();
        for (PhongChieu pc : oldList) {
            model.addRow(new Object[] {
                pc.getMaPhongChieu(),
                pc.getTenPhongChieu(),
                pc.getSoGheNgoi(),
                pc.getTrangThaiPhong(),
                pc.getLoaiManHinh(),
                pc.getHeThongAmThanh()
            });
        }
    }
    
    private void updateDatabase() {
        List<PhongChieu> newList = new ArrayList<>();
        for (int i = 0; i < model.getRowCount(); i++) {
            PhongChieu pc = new PhongChieu();
            pc.setMaPhongChieu(Integer.parseInt(model.getValueAt(i, 0).toString()));
            pc.setTenPhongChieu(model.getValueAt(i, 1).toString());
            pc.setSoGheNgoi(Integer.parseInt(model.getValueAt(i, 2).toString()));
            pc.setTrangThaiPhong(model.getValueAt(i, 3).toString());
            pc.setLoaiManHinh(model.getValueAt(i, 4).toString());
            pc.setHeThongAmThanh(model.getValueAt(i, 5).toString());
            newList.add(pc);
        }
        new PhongChieuDAO().updateDatabase(newList);
    }
    
    private void searchPhongChieuByID() {
        String searchID = txtTimKiem.getText().trim();
        if (searchID.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập mã phòng chiếu để tìm kiếm!");
            return;
        }
        tblPhongChieu.clearSelection();
        boolean found = false;
        for (int row = 0; row < model.getRowCount(); row++) {
            Object idValue = model.getValueAt(row, 0); 
            if (idValue != null && idValue.toString().equals(searchID)) {
                tblPhongChieu.setRowSelectionInterval(row, row);
                tblPhongChieu.scrollRectToVisible(new Rectangle(tblPhongChieu.getCellRect(row, 0, true)));
                found = true;
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy Phòng Chiếu có mã: " + searchID);
        }
    }
    
    private void searchPhongChieuByName() {
        String searchName = txtTimKiem.getText().trim();
        if (searchName.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tên phòng để tìm kiếm!");
            return;
        }
        tblPhongChieu.clearSelection();
        boolean found = false;
        for (int row = 0; row < model.getRowCount(); row++) {
            Object idValue = model.getValueAt(row, 1);
            if (idValue != null && idValue.toString().equals(searchName)) {
                tblPhongChieu.setRowSelectionInterval(row, row);
                tblPhongChieu.scrollRectToVisible(new Rectangle(tblPhongChieu.getCellRect(row, 0, true)));
                found = true;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Không tìm thấy Phòng Chiếu có tên: " + searchName);
        }
    }
    
    private void styleButton(JButton btn, Color bg) {
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setForeground(Color.WHITE);
        btn.setBackground(bg);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder(6, 15, 6, 15)); 
    }
    
}
