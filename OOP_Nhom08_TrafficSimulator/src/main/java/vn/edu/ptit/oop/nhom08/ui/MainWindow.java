package vn.edu.ptit.oop.nhom08.ui;

import java.awt.BorderLayout;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import vn.edu.ptit.oop.nhom08.common.BaseWindow;
import vn.edu.ptit.oop.nhom08.controller.LightController;
import vn.edu.ptit.oop.nhom08.model.Vehicle;
import vn.edu.ptit.oop.nhom08.simulation.Simulation;

public class MainWindow extends BaseWindow {

    private Simulation simulation;
    private IntersectionPanel intersectionPanel;

    public MainWindow() {
        super("Nhóm 08 - Mô phỏng Nút giao thông", 1200, 700);
    }

    @Override
    protected void initUI() {
        simulation = new Simulation();

        intersectionPanel = new IntersectionPanel(simulation);

        // 3. Toolbar
        addToolButton("/resources/icons/start.png", "Bắt đầu", e -> {
            simulation.start();
            setStatus("Mô phỏng đang chạy");
        });
        addToolButton("/resources/icons/pause.png", "Tạm dừng", e -> {
            simulation.pause();
            setStatus("Mô phỏng đã tạm dừng");
        });
        addToolButton("/resources/icons/reset.png", "Đặt lại", e -> {
            simulation.reset();
            intersectionPanel.repaint();
            setStatus("Đã đặt lại mô phỏng");
        });
        addToolButton("/resources/icons/add.png", "Thêm xe", e -> openAddVehicleDialog());

        JButton btnAdjustLight = new JButton("Chỉnh đèn");
        styleToolbarButton(btnAdjustLight);
        btnAdjustLight.addActionListener(e -> openAdjustLightDialog());
        toolBar.add(btnAdjustLight);

        initViewMenu();

        // 6. Cập nhật giao diện real-time
        simulation.setOnUpdate(() -> {
            intersectionPanel.repaint();

            int activeCount = simulation.getVehicles().size();
            long time = simulation.getSimulationTime();
            int passedCount = simulation.getPassedVehiclesCount();

            setStatus(String.format("Thời gian: %ds | Đang chờ/di chuyển: %d xe | Đã qua ngã tư: %d xe",
                                    time, activeCount, passedCount));
        });

        add(intersectionPanel, BorderLayout.CENTER);
        setStatus("TrafficSimulator đã sẵn sàng");
    }

    private void initViewMenu() {
        JMenuItem lightSettings = new JMenuItem("Cài đặt thời gian đèn...");
        lightSettings.setMnemonic(KeyEvent.VK_L);
        lightSettings.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));
        lightSettings.addActionListener(e -> openAdjustLightDialog());
        viewMenu.add(lightSettings);
    }

    private void openAdjustLightDialog() {
        boolean wasRunning = simulation.isRunning();
        simulation.pause();
        try {
            LightController controller = simulation.getTrafficLightController();
            SettingsDialog dialog = new SettingsDialog(this, controller);
            dialog.setVisible(true); // modal: chặn đến khi dialog đóng
            if (dialog.isConfirmed()) {
                intersectionPanel.repaint();
                setStatus(String.format("Đã cập nhật thời gian đèn: Xanh %ds | Vàng %ds | Đỏ %ds",
                        controller.getGreenDuration(), controller.getYellowDuration(),
                        controller.getRedDuration()));
            }
        } finally {
            if (wasRunning) {
                simulation.start();
            }
        }
    }

    private void openAddVehicleDialog() {
        boolean wasRunning = simulation.isRunning();
        simulation.pause();
        try {
            AddVehicleDialog dialog = new AddVehicleDialog(this, simulation);
            dialog.setVisible(true); // modal: chặn đến khi dialog đóng
            if (dialog.isConfirmed()) {
                Vehicle vehicle = dialog.getResult();
                try {
                    simulation.addVehicle(vehicle);
                    intersectionPanel.repaint();
                    setStatus("Đã thêm: " + vehicle.getId() + " (" + dialog.getSelectedType() + ")");
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage(), "Không thể thêm xe", JOptionPane.WARNING_MESSAGE);
                }
            }
        } finally {
            if (wasRunning) {
                simulation.start();
            }
        }
    }

    @Override
    protected void showAbout() {
        new AboutDialog(this).setVisible(true);
    }
}
