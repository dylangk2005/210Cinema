package view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class PanelChonGhe extends JDialog {

    private Map<JButton, String> seatMap = new HashMap<>();
    private Set<String> selectedSeats = new LinkedHashSet<>();
    private Set<String> bookedSeats = new HashSet<>();
    private JLabel lbSelected;
    private JButton btnConfirm, btnCancel;

    // ====== INTERFACE CALLBACK ======
    public interface SeatSelectionListener {
        void onSeatsSelected(Set<String> selectedSeats);
    }

    // ====== CONSTRUCTOR ======
    public PanelChonGhe(JFrame parent, SeatSelectionListener listener) {
        super(parent, "Chọn Ghế", true);
        setSize(800, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(15, 15));
        getContentPane().setBackground(Color.WHITE);

        // ====== MÀN HÌNH CHIẾU ======
        JPanel screenPanel = new JPanel(new BorderLayout());
        screenPanel.setBackground(Color.WHITE);
        screenPanel.setBorder(BorderFactory.createEmptyBorder(15, 40, 0, 40));

        JPanel blackScreen = new JPanel();
        blackScreen.setBackground(Color.BLACK);
        JLabel lbScreen = new JLabel("MÀN HÌNH CHIẾU PHIM", JLabel.CENTER);
        lbScreen.setFont(new Font("Arial", Font.BOLD, 20));
        lbScreen.setForeground(Color.WHITE);
        blackScreen.setPreferredSize(new Dimension(700, 50));
        blackScreen.setLayout(new BorderLayout());
        blackScreen.add(lbScreen, BorderLayout.CENTER);

        screenPanel.add(blackScreen, BorderLayout.CENTER);
        add(screenPanel, BorderLayout.NORTH);

        // ====== GHẾ ======
        JPanel seatPanel = new JPanel(new GridLayout(5, 10, 10, 10));
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        seatPanel.setBackground(Color.WHITE);

        char[] rows = {'A', 'B', 'C', 'D', 'E'};
        for (char row : rows) {
            for (int col = 1; col <= 10; col++) {
                String seatCode = String.format("%c%02d", row, col);
                JButton seatBtn = new JButton(seatCode);
                seatBtn.setFocusPainted(false);
                seatBtn.setBackground(Color.LIGHT_GRAY);
                seatBtn.setFont(new Font("Arial", Font.BOLD, 13));
                seatBtn.setForeground(Color.BLACK);
                seatBtn.setPreferredSize(new Dimension(55, 45));
                seatBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

                seatBtn.addActionListener(e -> toggleSeat(seatBtn));
                seatPanel.add(seatBtn);
                seatMap.put(seatBtn, seatCode);
            }
        }

        add(seatPanel, BorderLayout.CENTER);

        // ====== CHÚ THÍCH ======
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        legendPanel.setBackground(Color.WHITE);

        legendPanel.add(createLegend(Color.LIGHT_GRAY, "Ghế trống"));
        legendPanel.add(createLegend(new Color(0, 180, 0), "Ghế đang chọn"));
        legendPanel.add(createLegend(Color.RED, "Ghế đã đặt"));

        add(legendPanel, BorderLayout.SOUTH);

        // ====== PANEL DƯỚI ======
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
        bottomPanel.setBackground(Color.WHITE);

        lbSelected = new JLabel("Ghế đã chọn: Chưa chọn");
        lbSelected.setFont(new Font("Arial", Font.BOLD, 16));
        bottomPanel.add(lbSelected, BorderLayout.WEST);

        JPanel btnPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 5));
        btnPanel.setBackground(Color.WHITE);

        btnConfirm = new JButton("XÁC NHẬN");
        btnConfirm.setBackground(new Color(0, 150, 0));
        btnConfirm.setForeground(Color.WHITE);
        btnConfirm.setFont(new Font("Arial", Font.BOLD, 14));

        btnCancel = new JButton("HỦY");
        btnCancel.setBackground(new Color(200, 0, 0));
        btnCancel.setForeground(Color.WHITE);
        btnCancel.setFont(new Font("Arial", Font.BOLD, 14));

        btnPanel.add(btnCancel);
        btnPanel.add(btnConfirm);
        bottomPanel.add(btnPanel, BorderLayout.EAST);
        add(bottomPanel, BorderLayout.PAGE_END);

        // ====== EVENT ======
        btnCancel.addActionListener(e -> dispose());

        btnConfirm.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            listener.onSeatsSelected(selectedSeats);
            dispose();
        });

        // ====== GHẾ ĐÃ ĐẶT (GIẢ LẬP) ======
        bookedSeats.addAll(Arrays.asList("A03", "B05", "C07", "D10"));
        markBookedSeats();
    }

    // ====== HANDLE CHỌN GHẾ ======
    private void toggleSeat(JButton seatBtn) {
        String code = seatMap.get(seatBtn);
        if (bookedSeats.contains(code)) {
            Toolkit.getDefaultToolkit().beep();
            return;
        }

        if (selectedSeats.contains(code)) {
            selectedSeats.remove(code);
            seatBtn.setBackground(Color.LIGHT_GRAY);
        } else {
            selectedSeats.add(code);
            seatBtn.setBackground(new Color(0, 180, 0));
        }

        updateSelectedLabel();
    }

    private void updateSelectedLabel() {
        if (selectedSeats.isEmpty()) {
            lbSelected.setText("Ghế đã chọn: Chưa chọn");
        } else {
            lbSelected.setText("Ghế đã chọn: " + String.join(", ", selectedSeats));
        }
    }

    private void markBookedSeats() {
        for (var entry : seatMap.entrySet()) {
            if (bookedSeats.contains(entry.getValue())) {
                entry.getKey().setBackground(Color.RED);
                entry.getKey().setEnabled(false);
            }
        }
    }

    private JPanel createLegend(Color color, String text) {
        JPanel p = new JPanel(new FlowLayout(FlowLayout.LEFT, 5, 0));
        JPanel colorBox = new JPanel();
        colorBox.setBackground(color);
        colorBox.setPreferredSize(new Dimension(20, 20));
        p.add(colorBox);

        JLabel lb = new JLabel(text);
        lb.setFont(new Font("Arial", Font.PLAIN, 13));
        p.add(lb);

        p.setOpaque(false);
        return p;
    }
}
