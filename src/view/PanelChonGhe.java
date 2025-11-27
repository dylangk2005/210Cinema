package view;

import javax.swing.*;
import java.awt.*;
import java.util.*;
import dao.GheNgoiDAO;

public class PanelChonGhe extends JDialog {

    private Map<JButton, String> seatMap = new HashMap<>();
    private Map<String, Integer> seatCode_maGhe;
    private Set<String> selectedSeats = new LinkedHashSet<>();
    private Set<Integer> bookedSeats = new HashSet<>();
//    private JLabel lbSelected;
    private JTextArea taSelected;
    private JButton btnConfirm, btnCancel;

    // ====== INTERFACE CALLBACK ======
    public interface SeatSelectionListener {
        void onSeatsSelected(Set<String> selectedSeats, Set<Integer> listMaGhe);
    }

    // ====== CONSTRUCTOR ======
    public PanelChonGhe(int maPhongDaChon, int maSuatChieuDaChon, JFrame parent, SeatSelectionListener listener) {
        super(parent, "Chọn Ghế", true);
        setSize(1400, 800);
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
        JPanel seatPanel = new JPanel(new GridLayout(0, 12, 10, 10));   
        seatPanel.setBorder(BorderFactory.createEmptyBorder(20, 40, 20, 40));
        seatPanel.setBackground(Color.WHITE);
        
        seatCode_maGhe = new GheNgoiDAO().getAllSeats(maPhongDaChon);
        for (var entry : seatCode_maGhe.entrySet()) {
            String seatCode = entry.getKey();
            JButton seatBtn = new JButton(seatCode);
            seatBtn.setFocusPainted(false);
            seatBtn.setBackground(Color.LIGHT_GRAY);
            seatBtn.setFont(new Font("Arial", Font.BOLD, 13));
            seatBtn.setForeground(Color.BLACK);
            seatBtn.setPreferredSize(new Dimension(55, 45));
            seatBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            seatBtn.addActionListener(e -> toggleSeat(seatBtn));
            seatPanel.add(seatBtn);
            seatMap.put(seatBtn, entry.getKey());
        }
        
        add(seatPanel, BorderLayout.CENTER);

        // ====== CHÚ THÍCH ======
        JPanel legendPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 30, 10));
        legendPanel.setBackground(Color.WHITE);

        legendPanel.add(createLegend(Color.LIGHT_GRAY, "Ghế trống"));
        legendPanel.add(createLegend(new Color(0, 180, 0), "Ghế đang chọn"));
        legendPanel.add(createLegend(Color.RED, "Ghế đã đặt"));

        // ====== PANEL DƯỚI ======
        taSelected = new JTextArea("Ghế đã chọn: Chưa chọn");
        taSelected.setFont(new Font("Arial", Font.BOLD, 16));
        taSelected.setEditable(false);
        taSelected.setFocusable(false);
        taSelected.setOpaque(true);
        taSelected.setBackground(Color.WHITE); 
        taSelected.setLineWrap(true);
        taSelected.setWrapStyleWord(true);
        JScrollPane scrollSelected = new JScrollPane(
                taSelected,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );

        scrollSelected.setBorder(null);
        scrollSelected.setPreferredSize(new Dimension(1100, 55)); 
        
        JPanel bottomPanel = new JPanel(new BorderLayout(10, 10));
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 20, 10, 20));
        bottomPanel.setBackground(Color.WHITE);
        bottomPanel.add(scrollSelected, BorderLayout.WEST);

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
        
        // fix legendPanel ko hien thi
        JPanel bottomContainer = new JPanel();
        bottomContainer.setLayout(new BoxLayout(bottomContainer, BoxLayout.Y_AXIS));
        bottomContainer.setBackground(Color.WHITE);
        bottomContainer.add(legendPanel);
        bottomContainer.add(Box.createVerticalStrut(8));
        bottomContainer.add(bottomPanel);
        add(bottomContainer, BorderLayout.SOUTH);

        // ====== EVENT ======
        btnCancel.addActionListener(e -> dispose());

        btnConfirm.addActionListener(e -> {
            if (selectedSeats.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Vui lòng chọn ít nhất một ghế!", "Cảnh báo", JOptionPane.WARNING_MESSAGE);
                return;
            }
            Set<Integer> listMaGhe = new HashSet<>();
            for (String seat : selectedSeats) {
                listMaGhe.add(seatCode_maGhe.get(seat));
            }
            listener.onSeatsSelected(selectedSeats, listMaGhe);
            dispose();
        });

        bookedSeats = new GheNgoiDAO().getAllBookedSeats(maSuatChieuDaChon);
        markBookedSeats();
    }

    // ====== HANDLE CHỌN GHẾ ======
    private void toggleSeat(JButton seatBtn) {
        String code = seatMap.get(seatBtn);
//        if (bookedSeats.contains(code)) {
//            Toolkit.getDefaultToolkit().beep();
//            return;
//        }

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
            taSelected.setText("Ghế đã chọn: Chưa chọn");
        } else {
            taSelected.setText("Ghế đã chọn: " + String.join(", ", selectedSeats));
        }
    }
    
    private void markBookedSeats() {
        for (var entry : seatMap.entrySet()) {
            if (bookedSeats.contains(seatCode_maGhe.get(entry.getValue()))) {
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
