package view;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableCellRenderer;

public class PanelGioHang extends JDialog {
    private DefaultTableModel model;
    private JPanel panelContainer;
    private JTable tblSanPham;
    private JButton btnThanhToan, btnThoat, btnXacNhanCha;
    
    private final Color MAU_DO = new Color(180, 0, 0);
    private final Color TRANG = Color.WHITE;
    private final String[] col = {"Tên SP", "Số lượng", "Đơn giá"};

    public PanelGioHang(JFrame parent, List<JPanel> listVePanel, JPanel listPanelSP, JButton btn) {
        this.btnXacNhanCha = btn;
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
                JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

        scrollVe.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH VÉ", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        scrollVe.getViewport().setBackground(new Color(20,20,20));

        add(scrollVe, BorderLayout.CENTER);


        // ====== TABLE SẢN PHẨM ======
        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setBackground(new Color(20,20,20));

        JPanel spScroll = taoBang();
        for (Component c : listPanelSP.getComponents()) {
            if (c instanceof JPanel productPanel) {

                JLabel lb = (JLabel) productPanel.getComponent(0);
                JLabel dg = (JLabel) productPanel.getComponent(1);          
                JSpinner sl = (JSpinner) productPanel.getComponent(2);

                int soLuong = (int) sl.getValue();
                if (soLuong > 0) {
                    model.addRow(new Object[]{lb.getText(), soLuong, dg.getText()}); 
                }
            }
        }


        bottom.add(spScroll, BorderLayout.CENTER);

        // ====== FOOTER (THANH TOÁN) ======
        btnThoat = makeButton(80, 32, Color.DARK_GRAY, "Thoát");
        btnThoat.addActionListener(e -> dispose());
        
        btnThanhToan = makeButton(110, 32, MAU_DO, "Thanh toán");
        btnThanhToan.addActionListener(e -> {
            for (ActionListener al : btnXacNhanCha.getActionListeners()) {
                al.actionPerformed(new ActionEvent(btnThanhToan, ActionEvent.ACTION_PERFORMED, null));
            }
            dispose();
        });

        JPanel foot = new JPanel();
        foot.add(btnThoat);
        foot.add(btnThanhToan);

        bottom.add(foot, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);
    }
    
    
    private JPanel taoBang() {
        JPanel p = new JPanel(new BorderLayout());
        p.setBorder(new TitledBorder(new LineBorder(MAU_DO, 2),
                "DANH SÁCH SẢN PHẨM", TitledBorder.LEFT, TitledBorder.TOP,
                new Font("Segoe UI", Font.BOLD, 16), MAU_DO));

        model = new DefaultTableModel(col, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tblSanPham = new JTable(model);
        tblSanPham.setRowHeight(35);
        tblSanPham.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        DefaultTableCellRenderer header = new DefaultTableCellRenderer();
        header.setBackground(MAU_DO); header.setForeground(TRANG);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < tblSanPham.getColumnCount(); i++) 
            tblSanPham.getColumnModel().getColumn(i).setHeaderRenderer(header);

        p.add(new JScrollPane(tblSanPham), BorderLayout.CENTER);
        return p;
    }
    
    private JButton makeButton(int w, int h, Color bg, String value) {
        JButton btn = new JButton(value);
        btn.setPreferredSize(new Dimension(w, h));
        btn.setMaximumSize(new Dimension(w, h));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 13));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return btn;
    }
}


