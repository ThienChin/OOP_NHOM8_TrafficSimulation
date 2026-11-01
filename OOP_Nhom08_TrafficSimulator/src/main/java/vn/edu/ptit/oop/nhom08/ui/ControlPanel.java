package vn.edu.ptit.oop.nhom08.ui;

import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

public class ControlPanel extends JPanel {
    public ControlPanel(
            Runnable onStart,
            Runnable onPause,
            Runnable onReset,
            Runnable onAddVehicle) {

        setLayout(new FlowLayout(FlowLayout.CENTER, 12, 8));

        JButton startButton = new JButton("Bắt đầu");
        JButton pauseButton = new JButton("Tạm dừng");
        JButton resetButton = new JButton("Đặt lại");
        JButton addButton = new JButton("Thêm phương tiện");

        startButton.addActionListener(e -> onStart.run());
        pauseButton.addActionListener(e -> onPause.run());
        resetButton.addActionListener(e -> onReset.run());
        addButton.addActionListener(e -> onAddVehicle.run());

        add(startButton);
        add(pauseButton);
        add(resetButton);
        add(addButton);
    }
}