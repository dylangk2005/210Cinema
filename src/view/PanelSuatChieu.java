/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */

package view;

import dao.SuatChieuDAO;
import dao.PhimDAO;
import model.SuatChieu;
import model.Phim;
import java.awt.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.text.ParseException;
import java.util.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
public class PanelSuatChieu extends javax.swing.JFrame {
    
    private static final java.util.logging.Logger logger = java.util.logging.Logger.getLogger(PanelSuatChieu.class.getName());
        private SuatChieuDAO suatChieuDAO;
        private PhimDAO phimDAO;
        private DefaultTableModel tableModel;
    
        private Map<String, Integer> phimMap = new HashMap<>();
        private Map<String, Integer> phongMap = new HashMap<>();
    public PanelSuatChieu() {
        initComponents();
        init();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        panelTimKiem = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtSearchNgay = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        cbSearchPhim = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        cbSearchPhong = new javax.swing.JComboBox<>();
        btnTimKiem = new javax.swing.JButton();
        btnLamMoi = new javax.swing.JButton();
        panelInfo = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtMa = new javax.swing.JTextField();
        txtGiaVe = new javax.swing.JTextField();
        txtNgayChieu = new javax.swing.JTextField();
        cbPhim = new javax.swing.JComboBox<>();
        cbPhongChieu = new javax.swing.JComboBox<>();
        cbGIoChieu = new javax.swing.JComboBox<>();
        btnThem = new javax.swing.JButton();
        btnSua = new javax.swing.JButton();
        btnXoa = new javax.swing.JButton();
        btnXoaRong = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tableSuatChieu = new javax.swing.JTable();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        panelTimKiem.setBackground(new java.awt.Color(102, 102, 102));
        panelTimKiem.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Tìm kiếm & Lọc suất chiếu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setText("Ngày chiếu:");

        txtSearchNgay.setBackground(new java.awt.Color(153, 153, 153));
        txtSearchNgay.setColumns(10);
        txtSearchNgay.setToolTipText("dd/MM/yyyy\n");
        txtSearchNgay.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtSearchNgayActionPerformed(evt);
            }
        });

        jLabel2.setBackground(new java.awt.Color(51, 51, 51));
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Phim:");

        cbSearchPhim.setBackground(new java.awt.Color(153, 153, 153));
        cbSearchPhim.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbSearchPhimActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(51, 51, 51));
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Phòng:");

        cbSearchPhong.setBackground(new java.awt.Color(153, 153, 153));

        btnTimKiem.setBackground(new java.awt.Color(0, 102, 102));
        btnTimKiem.setForeground(new java.awt.Color(255, 255, 255));
        btnTimKiem.setText("Tìm kiếm");
        btnTimKiem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimKiemActionPerformed(evt);
            }
        });

        btnLamMoi.setBackground(new java.awt.Color(0, 102, 102));
        btnLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setText("Làm mới");
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelTimKiemLayout = new javax.swing.GroupLayout(panelTimKiem);
        panelTimKiem.setLayout(panelTimKiemLayout);
        panelTimKiemLayout.setHorizontalGroup(
            panelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTimKiemLayout.createSequentialGroup()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtSearchNgay, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 43, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(cbSearchPhim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(58, 58, 58)
                .addComponent(jLabel3)
                .addGap(27, 27, 27)
                .addComponent(cbSearchPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 37, Short.MAX_VALUE))
            .addGroup(panelTimKiemLayout.createSequentialGroup()
                .addGap(115, 115, 115)
                .addComponent(btnTimKiem)
                .addGap(112, 112, 112)
                .addComponent(btnLamMoi)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelTimKiemLayout.setVerticalGroup(
            panelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelTimKiemLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtSearchNgay, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2)
                    .addComponent(cbSearchPhim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(cbSearchPhong, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 16, Short.MAX_VALUE)
                .addGroup(panelTimKiemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnLamMoi)
                    .addComponent(btnTimKiem))
                .addContainerGap())
        );

        panelInfo.setBackground(new java.awt.Color(102, 102, 102));
        panelInfo.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Thông tin suất chiếu\n", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Mã suất chiếu:");

        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Phòng chiếu:");

        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("Giờ chiếu:");

        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Phim:");

        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Ngày chiếu:");

        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Giá vé cơ bản");

        txtMa.setEditable(false);
        txtMa.setBackground(new java.awt.Color(153, 153, 153));

        txtGiaVe.setBackground(new java.awt.Color(153, 153, 153));

        txtNgayChieu.setBackground(new java.awt.Color(153, 153, 153));
        txtNgayChieu.setToolTipText("dd/MM/yyyy\n");

        cbPhim.setBackground(new java.awt.Color(153, 153, 153));

        cbPhongChieu.setBackground(new java.awt.Color(153, 153, 153));

        cbGIoChieu.setBackground(new java.awt.Color(153, 153, 153));
        cbGIoChieu.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { " ", "09:45", "12:00", "14:20", "16:40\t", "19:00", "21:20", "23:30" }));
        cbGIoChieu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cbGIoChieuActionPerformed(evt);
            }
        });

        btnThem.setBackground(new java.awt.Color(0, 102, 102));
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });

        btnSua.setBackground(new java.awt.Color(0, 102, 102));
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setText("Sửa");
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });

        btnXoa.setBackground(new java.awt.Color(0, 102, 102));
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText("Xóa");
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });

        btnXoaRong.setBackground(new java.awt.Color(0, 102, 102));
        btnXoaRong.setForeground(new java.awt.Color(255, 255, 255));
        btnXoaRong.setText("Xóa rỗng");
        btnXoaRong.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaRongActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout panelInfoLayout = new javax.swing.GroupLayout(panelInfo);
        panelInfo.setLayout(panelInfoLayout);
        panelInfoLayout.setHorizontalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, panelInfoLayout.createSequentialGroup()
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6))
                        .addGap(47, 47, 47)
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbPhongChieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(cbGIoChieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(btnThem)
                        .addGap(41, 41, 41)
                        .addComponent(btnSua)))
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7)
                            .addComponent(jLabel8)
                            .addComponent(jLabel9))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 91, Short.MAX_VALUE)
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtGiaVe, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(cbPhim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(txtNgayChieu, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 71, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(53, 53, 53))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(btnXoa)
                        .addGap(26, 26, 26)
                        .addComponent(btnXoaRong)
                        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        panelInfoLayout.setVerticalGroup(
            panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelInfoLayout.createSequentialGroup()
                .addGap(17, 17, 17)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel7)
                    .addComponent(txtMa, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cbPhim, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(jLabel8)
                            .addComponent(cbPhongChieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelInfoLayout.createSequentialGroup()
                                .addGap(24, 24, 24)
                                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel6)
                                    .addComponent(cbGIoChieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(panelInfoLayout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(jLabel9))))
                    .addGroup(panelInfoLayout.createSequentialGroup()
                        .addComponent(txtNgayChieu, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(18, 18, 18)
                        .addComponent(txtGiaVe, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(panelInfoLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnThem)
                    .addComponent(btnSua)
                    .addComponent(btnXoa)
                    .addComponent(btnXoaRong))
                .addContainerGap(29, Short.MAX_VALUE))
        );

        jScrollPane1.setBackground(new java.awt.Color(51, 51, 51));
        jScrollPane1.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Danh sách suất chiếu", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 12), new java.awt.Color(255, 255, 255))); // NOI18N

        tableSuatChieu.setBackground(new java.awt.Color(102, 102, 102));
        tableSuatChieu.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Mã suất chiếu", "Phim", "Phòng", "Ngày giờ chiếu", "Giá vé"
            }
        ) {
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        tableSuatChieu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableSuatChieuMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tableSuatChieu);
        if (tableSuatChieu.getColumnModel().getColumnCount() > 0) {
            tableSuatChieu.getColumnModel().getColumn(0).setResizable(false);
            tableSuatChieu.getColumnModel().getColumn(1).setResizable(false);
            tableSuatChieu.getColumnModel().getColumn(2).setResizable(false);
            tableSuatChieu.getColumnModel().getColumn(3).setResizable(false);
            tableSuatChieu.getColumnModel().getColumn(4).setResizable(false);
        }

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(panelTimKiem, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(panelInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(panelTimKiem, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(panelInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>                        
    private void init() {
        suatChieuDAO = new SuatChieuDAO();
        phimDAO = new PhimDAO();
        tableModel = (DefaultTableModel) tableSuatChieu.getModel();
        // Style table header
        tableSuatChieu.getTableHeader().setBackground(new Color(41, 128, 185));
        tableSuatChieu.getTableHeader().setForeground(Color.WHITE);
        tableSuatChieu.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        
        setupDateFields();
        loadComboBoxData();
        loadData();
    }
    
    // Setup placeholder cho các TextField ngày
    private void setupDateFields() {
        txtNgayChieu.setToolTipText("Nhập ngày: dd/MM/yyyy (VD: 25/12/2024)");
        txtSearchNgay.setToolTipText("Nhập ngày: dd/MM/yyyy");
        
        txtNgayChieu.setForeground(Color.GRAY);
        txtNgayChieu.setText("dd/MM/yyyy");
        
        txtSearchNgay.setForeground(Color.GRAY);
        txtSearchNgay.setText("dd/MM/yyyy");
        
        // Focus listeners cho txtNgayChieu
        txtNgayChieu.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtNgayChieu.getText().equals("dd/MM/yyyy")) {
                    txtNgayChieu.setText("");
                    txtNgayChieu.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtNgayChieu.getText().isEmpty()) {
                    txtNgayChieu.setForeground(Color.GRAY);
                    txtNgayChieu.setText("dd/MM/yyyy");
                }
            }
        });
        
        // Focus listeners cho txtSearchNgay
        txtSearchNgay.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearchNgay.getText().equals("dd/MM/yyyy")) {
                    txtSearchNgay.setText("");
                    txtSearchNgay.setForeground(Color.BLACK);
                }
            }
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearchNgay.getText().isEmpty()) {
                    txtSearchNgay.setForeground(Color.GRAY);
                    txtSearchNgay.setText("dd/MM/yyyy");
                }
            }
        });
    }
    
    // Parse ngày từ TextField
    private Date parseDateFromTextField(JTextField textField) {
        String dateStr = textField.getText().trim();
        if (dateStr.isEmpty() || dateStr.equals("dd/MM/yyyy")) {
            return null;
        }
        
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        try {
            return sdf.parse(dateStr);
        } catch (ParseException e) {
            JOptionPane.showMessageDialog(this, 
                "Ngày không hợp lệ! Định dạng: dd/MM/yyyy\nVí dụ: 25/12/2024", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }
    
    // Set ngày vào TextField
    private void setDateToTextField(JTextField textField, Date date) {
        if (date == null) {
            textField.setForeground(Color.GRAY);
            textField.setText("dd/MM/yyyy");
        } else {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            textField.setForeground(Color.BLACK);
            textField.setText(sdf.format(date));
        }
    }
    
    // Load dữ liệu cho ComboBox
    private void loadComboBoxData() {
        // Load phim
        cbPhim.removeAllItems();
        cbSearchPhim.removeAllItems();
        cbPhim.addItem("");
        cbSearchPhim.addItem("Tất cả");
        
        java.util.List<Phim> phimList = phimDAO.selectAll();
        for (Phim phim : phimList) {
            String item = phim.getMaPhim() + " - " + phim.getTenPhim();
            cbPhim.addItem(item);
            cbSearchPhim.addItem(item);
            phimMap.put(item, phim.getMaPhim());
        }
        
        // Load phòng chiếu
        cbPhongChieu.removeAllItems();
        cbSearchPhong.removeAllItems();
        cbPhongChieu.addItem("");
        cbSearchPhong.addItem("Tất cả");
        
        String[] rooms = {"1 - Phòng 1", "2 - Phòng 2", "3 - Phòng 3", 
                          "4 - Phòng 4", "5 - Phòng VIP"};
        for (String room : rooms) {
            cbPhongChieu.addItem(room);
            cbSearchPhong.addItem(room);
            phongMap.put(room, Integer.parseInt(room.split(" - ")[0]));
        }
    }
    
    // Load dữ liệu từ database
    private void loadData() {
        tableModel.setRowCount(0);
        java.util.List<SuatChieu> list = suatChieuDAO.selectAll();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (SuatChieu sc : list) {
            tableModel.addRow(new Object[]{
                sc.getMaSuatChieu(),
                sc.getTenPhim(),
                sc.getTenPhongChieu(),
                sdf.format(sc.getNgayGioChieu()),
                String.format("%,.0f đ", sc.getGiaVeCoBan())
            });
        }
    }
    
    // Hiển thị thông tin suất chiếu lên form
    private void displayShowtimeInfo(int row) {
        int maSuatChieu = Integer.parseInt(tableModel.getValueAt(row, 0).toString());
        SuatChieu sc = suatChieuDAO.selectById(maSuatChieu);
        
        if (sc != null) {
            txtMa.setText(String.valueOf(sc.getMaSuatChieu()));
            
            // Chọn phim
            for (int i = 0; i < cbPhim.getItemCount(); i++) {
                String item = (String) cbPhim.getItemAt(i);
                if (phimMap.containsKey(item) && phimMap.get(item) == sc.getMaPhim()) {
                    cbPhim.setSelectedIndex(i);
                    break;
                }
            }
            
            // Chọn phòng
            for (int i = 0; i < cbPhongChieu.getItemCount(); i++) {
                String item = (String) cbPhongChieu.getItemAt(i);
                if (phongMap.containsKey(item) && phongMap.get(item) == sc.getMaPhongChieu()) {
                    cbPhongChieu.setSelectedIndex(i);
                    break;
                }
            }
            
            // Set ngày và giờ
            setDateToTextField(txtNgayChieu, sc.getNgayGioChieu());
            
            SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
            String time = timeFormat.format(sc.getNgayGioChieu());
            cbGioChieu.setSelectedItem(time);
            
            txtGiaVe.setText(sc.getGiaVeCoBan().toString());
        }
    }
    
    // Thêm suất chiếu mới
    private void insertShowtime() {
        if (!validateInput()) {
            return;
        }
        
        SuatChieu sc = getShowtimeFromForm();
        
        if (suatChieuDAO.insert(sc)) {
            JOptionPane.showMessageDialog(this, 
                "Thêm suất chiếu thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Thêm suất chiếu thất bại!\nCó thể do trùng lịch chiếu.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Cập nhật suất chiếu
    private void updateShowtime() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn suất chiếu cần sửa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) {
            return;
        }
        
        SuatChieu sc = getShowtimeFromForm();
        sc.setMaSuatChieu(Integer.parseInt(txtMa.getText().trim()));
        
        if (suatChieuDAO.update(sc)) {
            JOptionPane.showMessageDialog(this, 
                "Cập nhật suất chiếu thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Cập nhật suất chiếu thất bại!\nCó thể do trùng lịch chiếu.", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // Xóa suất chiếu
    private void deleteShowtime() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn suất chiếu cần xóa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa suất chiếu này?", 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int maSuatChieu = Integer.parseInt(txtMa.getText().trim());
            
            if (suatChieuDAO.delete(maSuatChieu)) {
                JOptionPane.showMessageDialog(this, 
                    "Xóa suất chiếu thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa suất chiếu thất bại!\nCó thể đã có khách mua vé cho suất chiếu này.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    // Tìm kiếm suất chiếu
    private void searchShowtimes() {
        Date ngay = parseDateFromTextField(txtSearchNgay);
        
        Integer maPhim = null;
        String selectedPhim = (String) cbSearchPhim.getSelectedItem();
        if (selectedPhim != null && !selectedPhim.equals("Tất cả") && phimMap.containsKey(selectedPhim)) {
            maPhim = phimMap.get(selectedPhim);
        }
        
        Integer maPhong = null;
        String selectedPhong = (String) cbSearchPhong.getSelectedItem();
        if (selectedPhong != null && !selectedPhong.equals("Tất cả") && phongMap.containsKey(selectedPhong)) {
            maPhong = phongMap.get(selectedPhong);
        }
        
        java.util.List<SuatChieu> list = suatChieuDAO.search(ngay, maPhim, maPhong);
        
        tableModel.setRowCount(0);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        
        for (SuatChieu sc : list) {
            tableModel.addRow(new Object[]{
                sc.getMaSuatChieu(),
                sc.getTenPhim(),
                sc.getTenPhongChieu(),
                sdf.format(sc.getNgayGioChieu()),
                String.format("%,.0f đ", sc.getGiaVeCoBan())
            });
        }
        
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy suất chiếu phù hợp!", 
                "Kết quả tìm kiếm", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Tìm thấy " + list.size() + " suất chiếu!", 
                "Kết quả tìm kiếm", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Lấy thông tin suất chiếu từ form
    private SuatChieu getShowtimeFromForm() {
        SuatChieu sc = new SuatChieu();
        
        // Lấy mã phim
        String selectedPhim = (String) cbPhim.getSelectedItem();
        sc.setMaPhim(phimMap.get(selectedPhim));
        
        // Lấy mã phòng
        String selectedPhong = (String) cbPhongChieu.getSelectedItem();
        sc.setMaPhongChieu(phongMap.get(selectedPhong));
        
        // Kết hợp ngày và giờ
        Date date = parseDateFromTextField(txtNgayChieu);
        String time = (String) cbGioChieu.getSelectedItem();
        String[] timeParts = time.split(":");
        
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(timeParts[0]));
        cal.set(Calendar.MINUTE, Integer.parseInt(timeParts[1]));
        cal.set(Calendar.SECOND, 0);
        
        sc.setNgayGioChieu(cal.getTime());
        sc.setGiaVeCoBan(new BigDecimal(txtGiaVe.getText().trim()));
        
        return sc;
    }
    
    // Xóa rỗng form
    private void clearForm() {
        txtMa.setText("");
        cbPhim.setSelectedIndex(0);
        cbPhongChieu.setSelectedIndex(0);
        setDateToTextField(txtNgayChieu, null);
        cbGioChieu.setSelectedIndex(0);
        txtGiaVe.setText("");
        tableSuatChieu.clearSelection();
    }
    
    // Validate dữ liệu nhập
    private boolean validateInput() {
        if (cbPhim.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn phim!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (cbPhongChieu.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn phòng chiếu!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        Date ngay = parseDateFromTextField(txtNgayChieu);
        if (ngay == null) {
            return false;
        }
        
        if (cbGioChieu.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn giờ chiếu!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            BigDecimal gia = new BigDecimal(txtGiaVe.getText().trim());
            if (gia.compareTo(BigDecimal.ZERO) <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Giá vé phải lớn hơn 0!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                txtGiaVe.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Giá vé phải là số!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtGiaVe.requestFocus();
            return false;
        }
        
        return true;
    }
    private void txtSearchNgayActionPerformed(java.awt.event.ActionEvent evt) {                                              
        // TODO add your handling code here:
    }                                             

    private void cbSearchPhimActionPerformed(java.awt.event.ActionEvent evt) {                                             
        // TODO add your handling code here:
    }                                            

    private void cbGIoChieuActionPerformed(java.awt.event.ActionEvent evt) {                                           
        // TODO add your handling code here:
    }                                          

    private void btnTimKiemActionPerformed(java.awt.event.ActionEvent evt) {                                           
        searchShowtimes();
    }                                          

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {                                          
        setDateToTextField(txtSearchNgay, null);
        cbSearchPhim.setSelectedIndex(0);
         cbSearchPhong.setSelectedIndex(0);
        loadData();
    }                                         

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {                                        
        insertShowtime();
    }                                       

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {                                       
        updateShowtime();
    }                                      

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {                                       
        deleteShowtime();
    }                                      

    private void btnXoaRongActionPerformed(java.awt.event.ActionEvent evt) {                                           
        clearForm();
    }                                          

    private void tableSuatChieuMouseClicked(java.awt.event.MouseEvent evt) {                                            
        int selectedRow = tableSuatChieu.getSelectedRow();
        if (selectedRow >= 0) {
            displayShowtimeInfo(selectedRow);
        }
    }                                           

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ReflectiveOperationException | javax.swing.UnsupportedLookAndFeelException ex) {
            logger.log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(() -> new PanelSuatChieu().setVisible(true));
    }

    // Variables declaration - do not modify                     
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnTimKiem;
    private javax.swing.JButton btnXoa;
    private javax.swing.JButton btnXoaRong;
    private javax.swing.JComboBox<String> cbGIoChieu;
    private javax.swing.JComboBox<String> cbPhim;
    private javax.swing.JComboBox<String> cbPhongChieu;
    private javax.swing.JComboBox<String> cbSearchPhim;
    private javax.swing.JComboBox<String> cbSearchPhong;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel panelInfo;
    private javax.swing.JPanel panelTimKiem;
    private javax.swing.JTable tableSuatChieu;
    private javax.swing.JTextField txtGiaVe;
    private javax.swing.JTextField txtMa;
    private javax.swing.JTextField txtNgayChieu;
    private javax.swing.JTextField txtSearchNgay;
    // End of variables declaration                   
}
