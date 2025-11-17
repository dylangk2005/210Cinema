package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

public class PanelSuatChieu extends JPanel {

    private JTextField txtMaSuat, txtNgayChieu, txtThoiGian;
    private JComboBox<String> cbPhim, cbPhong;
    private JTable table;
    private DefaultTableModel model;

    public PanelSuatChieu() {
        setLayout(new BorderLayout(10, 10));

        // ====== Tiêu đề ======
        JLabel title = new JLabel("Quản Lý Suất Chiếu", JLabel.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 26));
        title.setForeground(new Color(0, 80, 80));
        add(title, BorderLayout.NORTH);

        // ====== Panel trung tâm chứa form + bảng ======
        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        add(centerPanel, BorderLayout.CENTER);

        // ====== Panel trái: TreeView quản lý suất chiếu ======
        DefaultMutableTreeNode root = new DefaultMutableTreeNode("Quản lý suất chiếu");
        DefaultMutableTreeNode ngay1 = new DefaultMutableTreeNode("Ngày chiếu: 02/05");
        ngay1.add(new DefaultMutableTreeNode("SC001"));
        ngay1.add(new DefaultMutableTreeNode("SC002"));
        DefaultMutableTreeNode ngay2 = new DefaultMutableTreeNode("Ngày chiếu: 03/05");
        ngay2.add(new DefaultMutableTreeNode("SC003"));
        root.add(ngay1);
        root.add(ngay2);

        JTree tree = new JTree(root);
        JScrollPane spTree = new JScrollPane(tree);
        spTree.setPreferredSize(new Dimension(200, 300));
        centerPanel.add(spTree, BorderLayout.WEST);

        // ====== Panel nhập thông tin ======
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBorder(BorderFactory.createTitledBorder("Thông tin suất chiếu"));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        JLabel lbMa = new JLabel("Mã suất:");
        JLabel lbPhim = new JLabel("Phim:");
        JLabel lbPhong = new JLabel("Phòng:");
        JLabel lbNgay = new JLabel("Ngày chiếu:");
        JLabel lbThoiGian = new JLabel("Thời gian:");

        txtMaSuat = new JTextField();
        cbPhim = new JComboBox<>(new String[]{"P001", "P002", "P003"});
        cbPhong = new JComboBox<>(new String[]{"Phòng 1", "Phòng 2", "Phòng 3"});
        txtNgayChieu = new JTextField("12/05/2025");
        txtThoiGian = new JTextField("10:00");

        gbc.gridx = 0; gbc.gridy = 0;
        formPanel.add(lbMa, gbc);
        gbc.gridx = 1; formPanel.add(txtMaSuat, gbc);

        gbc.gridx = 2;
        formPanel.add(lbPhim, gbc);
        gbc.gridx = 3; formPanel.add(cbPhim, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        formPanel.add(lbPhong, gbc);
        gbc.gridx = 1; formPanel.add(cbPhong, gbc);

        gbc.gridx = 2;
        formPanel.add(lbNgay, gbc);
        gbc.gridx = 3; formPanel.add(txtNgayChieu, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        formPanel.add(lbThoiGian, gbc);
        gbc.gridx = 1; gbc.gridwidth = 3;
        formPanel.add(txtThoiGian, gbc);

        centerPanel.add(formPanel, BorderLayout.NORTH);

        // ====== Bảng danh sách suất chiếu ======
        model = new DefaultTableModel(
                new Object[]{"Mã suất chiếu", "Phim", "Phòng", "Giờ chiếu", "Ngày"}, 0);
        table = new JTable(model);
        JScrollPane spTable = new JScrollPane(table);
        centerPanel.add(spTable, BorderLayout.CENTER);

        // ====== Panel nút chức năng ======
        JPanel bottom = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));
        JTextField txtSearch = new JTextField(15);
        JButton btnTim = new JButton("Tìm kiếm");
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnClear = new JButton("Xóa Rỗng");

        bottom.add(btnTim);
        bottom.add(txtSearch);
        bottom.add(btnThem);
        bottom.add(btnSua);
        bottom.add(btnXoa);
        bottom.add(btnClear);
        add(bottom, BorderLayout.SOUTH);

        // ====== Sự kiện ======
        btnThem.addActionListener(e -> {
            model.addRow(new Object[]{
                    txtMaSuat.getText(),
                    cbPhim.getSelectedItem(),
                    cbPhong.getSelectedItem(),
                    txtThoiGian.getText(),
                    txtNgayChieu.getText()
            });
        });

        btnSua.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                model.setValueAt(txtMaSuat.getText(), i, 0);
                model.setValueAt(cbPhim.getSelectedItem(), i, 1);
                model.setValueAt(cbPhong.getSelectedItem(), i, 2);
                model.setValueAt(txtThoiGian.getText(), i, 3);
                model.setValueAt(txtNgayChieu.getText(), i, 4);
            }
        });

        btnXoa.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) model.removeRow(i);
        });

        btnClear.addActionListener(e -> {
            txtMaSuat.setText("");
            txtNgayChieu.setText("");
            txtThoiGian.setText("");
        });

        btnTim.addActionListener(e -> {
            String ma = txtSearch.getText().trim();
            for (int i = 0; i < model.getRowCount(); i++) {
                if (model.getValueAt(i, 0).toString().equalsIgnoreCase(ma)) {
                    table.setRowSelectionInterval(i, i);
                    table.scrollRectToVisible(table.getCellRect(i, 0, true));
                    break;
                }
            }
        });

        // Click bảng -> đổ dữ liệu lên form
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                if (i >= 0) {
                    txtMaSuat.setText(model.getValueAt(i, 0).toString());
                    cbPhim.setSelectedItem(model.getValueAt(i, 1));
                    cbPhong.setSelectedItem(model.getValueAt(i, 2));
                    txtThoiGian.setText(model.getValueAt(i, 3).toString());
                    txtNgayChieu.setText(model.getValueAt(i, 4).toString());
                }
            }
        });
    }
}
