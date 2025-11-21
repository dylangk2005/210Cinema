package view;

import dao.PhimDAO;
import model.Phim;
import util.DBConnection;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

public class PanelPhim extends JPanel {

    private JTextField txtMa, txtTen, txtThoiLuong, txtSearchName;
    private JComboBox<String> cbTheLoai, cbQuocGia, cbSearchGenre;
    private DefaultTableModel model;
    private JTable table;
    
    private PhimDAO phimDAO;

    public PanelPhim() {
        this.phimDAO = new PhimDAO();
        
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // Panels
        JPanel panelSearch = createSearchPanel();
        JPanel panelInfo = createInfoPanel();
        JPanel panelButtons = createButtonPanel();
        JPanel panelTable = createTablePanel();

        // Layout
        JPanel topPanel = new JPanel(new BorderLayout(5, 5));
        topPanel.add(panelSearch, BorderLayout.NORTH);
        topPanel.add(panelInfo, BorderLayout.CENTER);
        topPanel.add(panelButtons, BorderLayout.SOUTH);
        
        add(topPanel, BorderLayout.NORTH);
        add(panelTable, BorderLayout.CENTER);

        loadData();
    }
    
    private JPanel createSearchPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Tìm kiếm phim"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        
        // Movie name
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Tên phim:"), gbc);
        
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.4;
        txtSearchName = new JTextField(20);
        panel.add(txtSearchName, gbc);
        
        // Genre
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0;
        panel.add(new JLabel("Thể loại:"), gbc);
        
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.3;
        cbSearchGenre = new JComboBox<>(new String[]{
            "Tất cả", "Hành động", "Tình cảm", "Hài hước", "Kinh dị"
        });
        panel.add(cbSearchGenre, gbc);
        
        // Search button
        gbc.gridx = 4; gbc.gridy = 0; gbc.weightx = 0;
        JButton btnSearch = new JButton("Tìm kiếm");
        btnSearch.setBackground(new Color(52, 152, 219));
        btnSearch.setForeground(Color.WHITE);
        btnSearch.setPreferredSize(new Dimension(100, 30));
        btnSearch.addActionListener(e -> searchMovies());
        panel.add(btnSearch, gbc);
        
        // Reset button
        gbc.gridx = 5; gbc.gridy = 0;
        JButton btnReset = new JButton("Làm mới");
        btnReset.setBackground(new Color(149, 165, 166));
        btnReset.setForeground(Color.WHITE);
        btnReset.setPreferredSize(new Dimension(100, 30));
        btnReset.addActionListener(e -> {
            txtSearchName.setText("");
            cbSearchGenre.setSelectedIndex(0);
            loadData();
        });
        panel.add(btnReset, gbc);
        
        return panel;
    }
    
    private JPanel createInfoPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder("Thông tin phim"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 10, 5, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lbMa = new JLabel("Mã phim:");
        JLabel lbTen = new JLabel("Tên phim:");
        JLabel lbTheLoai = new JLabel("Thể loại:");
        JLabel lbQuocGia = new JLabel("Quốc gia:");
        JLabel lbThoiLuong = new JLabel("Thời lượng (phút):");

        txtMa = new JTextField();
        txtMa.setEditable(false);
        txtMa.setBackground(Color.LIGHT_GRAY);
        
        txtTen = new JTextField();
        txtThoiLuong = new JTextField();
        cbTheLoai = new JComboBox<>(new String[]{"", "Hành động", "Tình cảm", "Hài hước", "Kinh dị"});
        cbQuocGia = new JComboBox<>(new String[]{"", "Việt Nam", "Mỹ", "Nhật", "Hàn Quốc", "Ý"});

        // Row 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panel.add(lbMa, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1; panel.add(txtMa, gbc);
        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; panel.add(lbTen, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1; panel.add(txtTen, gbc);

        // Row 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panel.add(lbTheLoai, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1; panel.add(cbTheLoai, gbc);
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0; panel.add(lbQuocGia, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1; panel.add(cbQuocGia, gbc);

        // Row 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; panel.add(lbThoiLuong, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1; panel.add(txtThoiLuong, gbc);
        
        return panel;
    }
    
    // Create Button Panel
    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        
        JButton btnAdd = createStyledButton("Thêm", new Color(46, 204, 113));
        btnAdd.addActionListener(e -> insertMovie());
        
        JButton btnUpdate = createStyledButton("Sửa", new Color(241, 196, 15));
        btnUpdate.addActionListener(e -> updateMovie());
        
        JButton btnDelete = createStyledButton("Xóa", new Color(231, 76, 60));
        btnDelete.addActionListener(e -> deleteMovie());
        
        JButton btnDetail = createStyledButton("Xem chi tiết", new Color(52, 152, 219));
        btnDetail.addActionListener(e -> showDetail());
        
        JButton btnClear = createStyledButton("Xóa rỗng", new Color(149, 165, 166));
        btnClear.addActionListener(e -> clearForm());
        
        panel.add(btnAdd);
        panel.add(btnUpdate);
        panel.add(btnDelete);
        panel.add(btnDetail);
        panel.add(btnClear);
        
        return panel;
    }
    
    // Create Styled Button
    private JButton createStyledButton(String text, Color color) {
        JButton button = new JButton(text);
        button.setBackground(color);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setPreferredSize(new Dimension(120, 35));
        button.setFont(new Font("Arial", Font.BOLD, 12));
        button.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                button.setBackground(color.brighter());
            }
            public void mouseExited(MouseEvent e) {
                button.setBackground(color);
            }
        });
        
        return button;
    }
    
    // Create Table Panel
    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(new TitledBorder("Danh sách phim"));
        
        model = new DefaultTableModel(
            new Object[]{"Mã phim", "Tên phim", "Thể loại", "Quốc gia", "Thời lượng (phút)"}, 0
        ) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        table = new JTable(model);
        table.setRowHeight(30);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(new Font("Arial", Font.PLAIN, 12));
        table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(41, 128, 185));
        table.getTableHeader().setForeground(Color.WHITE);
        
        // Column widths
        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(250);
        table.getColumnModel().getColumn(2).setPreferredWidth(120);
        table.getColumnModel().getColumn(3).setPreferredWidth(120);
        table.getColumnModel().getColumn(4).setPreferredWidth(120);
        
        // Table click event
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    displayMovieInfo(selectedRow);
                }
                
                // Double click for detail
                if (e.getClickCount() == 2) {
                    showDetail();
                }
            }
        });
        
        JScrollPane scrollPane = new JScrollPane(table);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        return panel;
    }
    
    // Load data from database
    private void loadData() {
        model.setRowCount(0);
        List<Phim> movies = phimDAO.selectAll();
        
        for (Phim phim : movies) {
            model.addRow(new Object[]{
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.gettheLoai(),
                phim.getGioiHanTuoi(),
                phim.getThoiLuong()
            });
        }
    }
    
    private void displayMovieInfo(int row) {
        int maPhim = Integer.parseInt(model.getValueAt(row, 0).toString());
        Phim phim = phimDAO.selectById(maPhim);
        
        if (phim != null) {
            txtMa.setText(String.valueOf(phim.getMaPhim()));
            txtTen.setText(phim.getTenPhim());
            cbTheLoai.setSelectedItem(phim.gettgettheLoai());
            cbQuocGia.setSelectedItem(phim.getGioiHanTuoi());
            txtThoiLuong.setText(String.valueOf(phim.getThoiLuong()));
        }
    }
    
    private void insertMovie() {
        if (!validateInput()) {
            return;
        }
        
        Phim phim = getMovieFromForm();
        
        if (phimDAO.insert(phim)) {
            JOptionPane.showMessageDialog(this, 
                "Thêm phim thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Thêm phim thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void updateMovie() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn phim cần sửa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        if (!validateInput()) {
            return;
        }
        
        Phim phim = getMovieFromForm();
        phim.setMaPhim(Integer.parseInt(txtMa.getText().trim()));
        
        if (phimDAO.update(phim)) {
            JOptionPane.showMessageDialog(this, 
                "Cập nhật phim thành công!", 
                "Thành công", 
                JOptionPane.INFORMATION_MESSAGE);
            loadData();
            clearForm();
        } else {
            JOptionPane.showMessageDialog(this, 
                "Cập nhật phim thất bại!", 
                "Lỗi", 
                JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void deleteMovie() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn phim cần xóa!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int confirm = JOptionPane.showConfirmDialog(this, 
            "Bạn có chắc chắn muốn xóa phim này?\n" +
            "Mã phim: " + txtMa.getText() + "\n" +
            "Tên phim: " + txtTen.getText(), 
            "Xác nhận xóa", 
            JOptionPane.YES_NO_OPTION,
            JOptionPane.WARNING_MESSAGE);
        
        if (confirm == JOptionPane.YES_OPTION) {
            int maPhim = Integer.parseInt(txtMa.getText().trim());
            
            if (phimDAO.delete(maPhim)) {
                JOptionPane.showMessageDialog(this, 
                    "Xóa phim thành công!", 
                    "Thành công", 
                    JOptionPane.INFORMATION_MESSAGE);
                loadData();
                clearForm();
            } else {
                JOptionPane.showMessageDialog(this, 
                    "Xóa phim thất bại!\nCó thể phim đang được sử dụng trong suất chiếu.", 
                    "Lỗi", 
                    JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    
    private void showDetail() {
        if (txtMa.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn phim cần xem chi tiết!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        int maPhim = Integer.parseInt(txtMa.getText().trim());
        Phim phim = phimDAO.selectById(maPhim);
        
        if (phim != null) {
            JDialog dialog = new JDialog((Frame) SwingUtilities.getWindowAncestor(this), 
                                        "Chi tiết phim", true);
            dialog.setSize(500, 400);
            dialog.setLocationRelativeTo(this);
            
            JPanel panel = new JPanel(new GridBagLayout());
            panel.setBorder(new EmptyBorder(20, 20, 20, 20));
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.anchor = GridBagConstraints.WEST;
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            String ngayChieu = phim.getNgayKhoiChieu() != null ? 
                              sdf.format(phim.getNgayKhoiChieu()) : "Chưa có";
            
            int row = 0;
            addDetailRow(panel, gbc, row++, "Mã phim:", String.valueOf(phim.getMaPhim()));
            addDetailRow(panel, gbc, row++, "Tên phim:", phim.getTenPhim());
            addDetailRow(panel, gbc, row++, "Thể loại:", phim.gettheLoai());
            addDetailRow(panel, gbc, row++, "Quốc gia:", phim.getGioiHanTuoi());
            addDetailRow(panel, gbc, row++, "Thời lượng:", phim.getThoiLuong() + " phút");
            addDetailRow(panel, gbc, row++, "Ngày khởi chiếu:", ngayChieu);
            addDetailRow(panel, gbc, row++, "Mô tả:", phim.getMoTa() != null ? phim.getMoTa() : "Không có");
            
            gbc.gridx = 0; gbc.gridy = row; gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JButton btnClose = new JButton("Đóng");
            btnClose.setPreferredSize(new Dimension(100, 30));
            btnClose.addActionListener(e -> dialog.dispose());
            panel.add(btnClose, gbc);
            
            dialog.add(new JScrollPane(panel));
            dialog.setVisible(true);
        }
    }
    
    private void addDetailRow(JPanel panel, GridBagConstraints gbc, int row, 
                              String label, String value) {
        gbc.gridx = 0; gbc.gridy = row; gbc.weightx = 0; gbc.gridwidth = 1;
        JLabel lbl = new JLabel(label);
        lbl.setFont(new Font("Arial", Font.BOLD, 13));
        panel.add(lbl, gbc);
        
        gbc.gridx = 1; gbc.weightx = 1;
        JLabel val = new JLabel(value);
        val.setFont(new Font("Arial", Font.PLAIN, 13));
        panel.add(val, gbc);
    }
    
    // Search movies by name and genre
    private void searchMovies() {
        String tenPhim = txtSearchName.getText().trim();
        String theLoai = cbSearchGenre.getSelectedItem().toString();
        
        if (theLoai.equals("Tất cả")) {
            theLoai = "";
        }
        
        List<Phim> movies = phimDAO.search(tenPhim, theLoai);
        
        model.setRowCount(0);
        for (Phim phim : movies) {
            model.addRow(new Object[]{
                phim.getMaPhim(),
                phim.getTenPhim(),
                phim.gettgettheLoai(),
                phim.getGioiHanTuoi(),
                phim.getThoiLuong()
            });
        }
        
        if (movies.isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Không tìm thấy phim phù hợp với điều kiện tìm kiếm!", 
                "Kết quả tìm kiếm", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Tìm thấy " + movies.size() + " phim!", 
                "Kết quả tìm kiếm", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    // Get movie from form
    private Phim getMovieFromForm() {
        Phim phim = new Phim();
        phim.setTenPhim(txtTen.getText().trim());
        phim.setThoiLuong(Integer.parseInt(txtThoiLuong.getText().trim()));
        phim.setTheLoai(cbTheLoai.getSelectedItem().toString());
        phim.setGioiHanTuoi(cbQuocGia.getSelectedItem().toString());
        phim.setNgayKhoiChieu(new Date(System.currentTimeMillis()));
        phim.setMoTa("");
        return phim;
    }
    
    // Clear form
    private void clearForm() {
        txtMa.setText("");
        txtTen.setText("");
        txtThoiLuong.setText("");
        cbTheLoai.setSelectedIndex(0);
        cbQuocGia.setSelectedIndex(0);
        table.clearSelection();
    }
    
    // Validate input
    private boolean validateInput() {
        if (txtTen.getText().trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng nhập tên phim!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtTen.requestFocus();
            return false;
        }
        
        if (cbTheLoai.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn thể loại!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        if (cbQuocGia.getSelectedIndex() == 0) {
            JOptionPane.showMessageDialog(this, 
                "Vui lòng chọn quốc gia!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            return false;
        }
        
        try {
            int thoiLuong = Integer.parseInt(txtThoiLuong.getText().trim());
            if (thoiLuong <= 0) {
                JOptionPane.showMessageDialog(this, 
                    "Thời lượng phải lớn hơn 0!", 
                    "Cảnh báo", 
                    JOptionPane.WARNING_MESSAGE);
                txtThoiLuong.requestFocus();
                return false;
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, 
                "Thời lượng phải là số nguyên!", 
                "Cảnh báo", 
                JOptionPane.WARNING_MESSAGE);
            txtThoiLuong.requestFocus();
            return false;
        }
        
        return true;
    }
}
