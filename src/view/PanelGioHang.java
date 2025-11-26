package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class PanelGioHang extends JDialog {

    private JPanel panelContainer;
    private JTable tblSanPham;

    public PanelGioHang(JFrame parent, List<JPanel> listVePanel, JPanel listPanelSP) {
        super(parent, "Giỏ hàng", true);
        initUI(listVePanel, listPanelSP);
        setSize(1400, 800);
        setLocationRelativeTo(parent);
    }

    private void initUI(List<JPanel> listVePanel, JPanel listPanelSP) {
        setLayout(new BorderLayout());

        // ======= CONTENT VÉ =======
        panelContainer = new JPanel();
        panelContainer.setBackground(Color.WHITE);
        panelContainer.setLayout(new BoxLayout(panelContainer, BoxLayout.X_AXIS));

        for (JPanel pnl : listVePanel) {
            pnl.setMaximumSize(new Dimension(300, 220));
            pnl.setPreferredSize(new Dimension(300, 220));
            panelContainer.add(pnl);
            panelContainer.add(Box.createRigidArea(new Dimension(15, 0)));
        }

        JScrollPane scrollVe = new JScrollPane(panelContainer,
                JScrollPane.VERTICAL_SCROLLBAR_NEVER,
                JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        scrollVe.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 40, 40)),
                "Vé đã chọn",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(255, 50, 50)
        ));

        scrollVe.getViewport().setBackground(new Color(20,20,20));

        add(scrollVe, BorderLayout.CENTER);


        // ====== TABLE SẢN PHẨM ======
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(20,20,20));

        String[] col = {"Tên SP", "Số lượng", "Đơn giá"};
        DefaultTableModel model = new DefaultTableModel(col, 0);
        tblSanPham = new JTable(model);

        tblSanPham.setRowHeight(28);
        tblSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        tblSanPham.setGridColor(new Color(80,80,80));

        for (Component c : listPanelSP.getComponents()) {
            if (c instanceof JPanel productPanel) {

                JLabel lb = (JLabel) productPanel.getComponent(0);
                JLabel dg = (JLabel) productPanel.getComponent(1);          
                JTextField tf = (JTextField) productPanel.getComponent(2);

                int soLuong = Integer.parseInt(tf.getText().trim());
                if (soLuong > 0) {
                    model.addRow(new Object[]{lb.getText(), soLuong, dg.getText()}); 
                }
            }
        }

        JScrollPane spScroll = new JScrollPane(tblSanPham);
        spScroll.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(255, 40, 40)),
                "Sản phẩm đã chọn",
                0, 0,
                new Font("Segoe UI", Font.BOLD, 14),
                new Color(255, 50, 50)
        ));

        bottom.add(spScroll, BorderLayout.CENTER);

        // ====== FOOTER (THANH TOÁN) ======
        JButton btnThanhToan = new JButton("Thoát");
        btnThanhToan.setBackground(new Color(200, 0, 0));
        btnThanhToan.setForeground(Color.WHITE);
        btnThanhToan.setFont(new Font("Segoe UI", Font.BOLD, 16));
        btnThanhToan.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        btnThanhToan.addActionListener(e -> dispose());

        JPanel foot = new JPanel();
        foot.add(btnThanhToan);

        bottom.add(foot, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
    }
}


