package view;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class PanelGioHang extends JDialog {

    private JTable tableItems;
    private JButton btnConfirm, btnCancel;
    private DefaultTableModel model;

    public interface CartListener {
        void onCartConfirmed();
        void onCartCanceled();
    }

    private CartListener listener;

    public void setCartListener(CartListener listener) {
        this.listener = listener;
    }

    public PanelGioHang(Frame parent) {
        super(parent, "Giỏ hàng", true);
        initUI();
    }

    private void initUI() {
        setSize(500, 400);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        JPanel panelMain = new JPanel(new BorderLayout());
        panelMain.setBorder(new TitledBorder("Danh sách sản phẩm"));

        model = new DefaultTableModel(new Object[]{"Tên", "Loại", "Số lượng", "Giá"}, 0);
        tableItems = new JTable(model);

        JScrollPane scroll = new JScrollPane(tableItems);
        panelMain.add(scroll, BorderLayout.CENTER);

        add(panelMain, BorderLayout.CENTER);

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

    public void addItem(String ten, String loai, int soLuong, double gia) {
        model.addRow(new Object[]{ten, loai, soLuong, gia});
    }

    public void clearItems() {
        model.setRowCount(0);
    }
}
