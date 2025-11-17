package view;

import java.awt.*;
import javax.swing.*;

public class PanelHome extends JPanel {

    public PanelHome() {
        setLayout(new BorderLayout());
        JLabel lb = new JLabel("CHÀO MỪNG ĐẾN VỚI 210CINEMA", JLabel.CENTER);
        lb.setFont(new Font("Arial", Font.BOLD, 30));
        lb.setForeground(new Color(0, 80, 80));
        add(lb, BorderLayout.CENTER);
    }
}
