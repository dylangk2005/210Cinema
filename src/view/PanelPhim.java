package view;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.DefaultTableModel;

public class PanelPhim extends JPanel {

    private JTextField txtMa, txtTen, txtThoiLuong, txtSearch;
    private JComboBox<String> cbTheLoai, cbQuocGia;
    private DefaultTableModel model;
    private JTable table;

    public PanelPhim() {
        setLayout(new BorderLayout(10, 10));
        setBorder(new EmptyBorder(15, 15, 15, 15));

        // === PANEL THÔNG TIN PHIM ===
        JPanel panelInfo = new JPanel(new GridBagLayout());
        panelInfo.setBorder(new TitledBorder("Thông tin phim"));
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
        txtTen = new JTextField();
        txtThoiLuong = new JTextField();
        cbTheLoai = new JComboBox<>(new String[]{"", "Hành động", "Tình cảm", "Hài hước", "Kinh dị"});
        cbQuocGia = new JComboBox<>(new String[]{"", "Việt Nam", "Mỹ", "Nhật", "Hàn Quốc", "Ý"});

        // Hàng 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0; panelInfo.add(lbMa, gbc);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 1; panelInfo.add(txtMa, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0; panelInfo.add(lbTen, gbc);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 1; panelInfo.add(txtTen, gbc);

        // Hàng 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0; panelInfo.add(lbTheLoai, gbc);
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 1; panelInfo.add(cbTheLoai, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0; panelInfo.add(lbQuocGia, gbc);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 1; panelInfo.add(cbQuocGia, gbc);

        // Hàng 3 (Thời lượng nằm riêng xuống dưới)
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0; panelInfo.add(lbThoiLuong, gbc);
        gbc.gridx = 1; gbc.gridy = 2; gbc.gridwidth = 3; gbc.weightx = 1; panelInfo.add(txtThoiLuong, gbc);
        gbc.gridwidth = 1;

        // === DANH SÁCH PHIM ===
        model = new DefaultTableModel(new Object[]{"Mã phim", "Tên phim", "Thể loại", "Quốc gia", "Thời lượng"}, 0);
        table = new JTable(model);
        table.setRowHeight(25);
        JScrollPane sp = new JScrollPane(table);

        // === KHU VỰC CHỨC NĂNG DƯỚI ===
        JPanel panelBottom = new JPanel(new GridBagLayout());
        GridBagConstraints gbc2 = new GridBagConstraints();
        gbc2.insets = new Insets(5, 5, 5, 5);
        gbc2.fill = GridBagConstraints.HORIZONTAL;

        txtSearch = new JTextField();
        JButton btnTim = new JButton("Tìm");
        JButton btnThem = new JButton("Thêm");
        JButton btnSua = new JButton("Sửa");
        JButton btnXoa = new JButton("Xóa");
        JButton btnClear = new JButton("Xóa rỗng");

        gbc2.gridx = 0; gbc2.weightx = 0.3; panelBottom.add(txtSearch, gbc2);
        gbc2.gridx = 1; gbc2.weightx = 0; panelBottom.add(btnTim, gbc2);
        gbc2.gridx = 2; panelBottom.add(btnThem, gbc2);
        gbc2.gridx = 3; panelBottom.add(btnSua, gbc2);
        gbc2.gridx = 4; panelBottom.add(btnXoa, gbc2);
        gbc2.gridx = 5; panelBottom.add(btnClear, gbc2);

        // === BỐ TRÍ TỔNG THỂ ===
        add(panelInfo, BorderLayout.NORTH);
        add(sp, BorderLayout.CENTER);
        add(panelBottom, BorderLayout.SOUTH);

        // === SỰ KIỆN CLICK BẢNG ===
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int i = table.getSelectedRow();
                txtMa.setText(model.getValueAt(i, 0).toString());
                txtTen.setText(model.getValueAt(i, 1).toString());
                cbTheLoai.setSelectedItem(model.getValueAt(i, 2).toString());
                cbQuocGia.setSelectedItem(model.getValueAt(i, 3).toString());
                txtThoiLuong.setText(model.getValueAt(i, 4).toString());
            }
        });

        // === NÚT CHỨC NĂNG ===
        btnThem.addActionListener(e -> {
            model.addRow(new Object[]{
                    txtMa.getText(), txtTen.getText(),
                    cbTheLoai.getSelectedItem(),
                    cbQuocGia.getSelectedItem(),
                    txtThoiLuong.getText()
            });
        });

        btnSua.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) {
                model.setValueAt(txtMa.getText(), i, 0);
                model.setValueAt(txtTen.getText(), i, 1);
                model.setValueAt(cbTheLoai.getSelectedItem(), i, 2);
                model.setValueAt(cbQuocGia.getSelectedItem(), i, 3);
                model.setValueAt(txtThoiLuong.getText(), i, 4);
            }
        });

        btnXoa.addActionListener(e -> {
            int i = table.getSelectedRow();
            if (i >= 0) model.removeRow(i);
        });

        btnClear.addActionListener(e -> {
            txtMa.setText("");
            txtTen.setText("");
            txtThoiLuong.setText("");
            cbTheLoai.setSelectedIndex(0);
            cbQuocGia.setSelectedIndex(0);
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
    }
}
