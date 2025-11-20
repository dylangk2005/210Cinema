package view;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;
import model.GioHang;

public class PanelGioHang extends JDialog {

    private JTable tableItems;
    private JButton btnConfirm, btnCancel;
    private DefaultTableModel model;

    private JLabel lbPhim, lbPhong, lbThoiLuong, lbTheLoai, lbThoiGianBD, lbGheDaChon;

    public interface CartListener {
        void onCartConfirmed();
        void onCartCanceled();
    }

    private CartListener listener;

    public void setCartListener(CartListener listener) {
        this.listener = listener;
    }

    public PanelGioHang(Frame parent,
                        String tenPhim,
                        String tenPhong,
                        String thoiLuong,
                        String theLoai,
                        String thoiGianBD,
                        String gheDaChon,
                        List<GioHang> items) {
        super(parent, "Giỏ hàng", true);
        initUI();

        lbPhim.setText(tenPhim);
        lbPhong.setText(tenPhong);
        lbThoiLuong.setText(thoiLuong);
        lbTheLoai.setText(theLoai);
        lbThoiGianBD.setText(thoiGianBD);
        lbGheDaChon.setText(gheDaChon);

        for (GioHang item : items) {
            addItem(item.getTen(), item.getSoLuong(), item.getGia());
        }
    }

    private void initUI() {
        setSize(600, 500);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelInfo = new JPanel(new GridLayout(6, 2, 10, 5));
        panelInfo.setBorder(new TitledBorder("Thông tin vé"));

        panelInfo.add(new JLabel("Tên phim:")); lbPhim = new JLabel(); panelInfo.add(lbPhim);
        panelInfo.add(new JLabel("Tên phòng:")); lbPhong = new JLabel(); panelInfo.add(lbPhong);
        panelInfo.add(new JLabel("Thời lượng:")); lbThoiLuong = new JLabel(); panelInfo.add(lbThoiLuong);
        panelInfo.add(new JLabel("Thể loại:")); lbTheLoai = new JLabel(); panelInfo.add(lbTheLoai);
        panelInfo.add(new JLabel("Thời gian BD:")); lbThoiGianBD = new JLabel(); panelInfo.add(lbThoiGianBD);
        panelInfo.add(new JLabel("Ghế đã chọn:")); lbGheDaChon = new JLabel(); panelInfo.add(lbGheDaChon);

        add(panelInfo, BorderLayout.NORTH);

        model = new DefaultTableModel(new Object[]{"Tên sản phẩm", "Số lượng", "Giá"}, 0);
        tableItems = new JTable(model);
        JScrollPane scroll = new JScrollPane(tableItems);
        add(scroll, BorderLayout.CENTER);

        JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnConfirm = new JButton("Xác nhận");
        btnCancel = new JButton("Hủy");

        btnConfirm.addActionListener(e -> {
            if (listener != null) listener.onCartConfirmed();
            dispose();
        });

        btnCancel.addActionListener(e -> {
            if (listener != null) listener.onCartCanceled();
            dispose();
        });

        panelButtons.add(btnConfirm);
        panelButtons.add(btnCancel);
        add(panelButtons, BorderLayout.SOUTH);
    }

    public void addItem(String ten, int soLuong, double gia) {
        model.addRow(new Object[]{ten, soLuong, gia});
    }

    public void clearItems() {
        model.setRowCount(0);
    }
}
