package vn.edu.ptit.oop.nhom08.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import vn.edu.ptit.oop.nhom08.controller.LightController;
import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;

/** Cấu hình thời lượng đèn xanh/vàng/đỏ (JDialog modal, dùng JSpinner). */
public class SettingsDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final LightController controller;
    private final JSpinner greenSpinner;
    private final JSpinner yellowSpinner;
    private final JSpinner redSpinner;
    private boolean confirmed = false;

    public SettingsDialog(Frame owner, LightController controller) {
        super(owner, "Cấu hình thời gian đèn giao thông", true);
        this.controller = controller;

        greenSpinner = new JSpinner(new SpinnerNumberModel(controller.getGreenDuration(), 1, 300, 1));
        yellowSpinner = new JSpinner(new SpinnerNumberModel(controller.getYellowDuration(), 1, 300, 1));
        redSpinner = new JSpinner(new SpinnerNumberModel(controller.getRedDuration(), 1, 300, 1));

        JPanel form = UiStyle.form(3);
        form.add(UiStyle.label("Đèn xanh (giây)")); form.add(greenSpinner);
        form.add(UiStyle.label("Đèn vàng (giây)")); form.add(yellowSpinner);
        form.add(UiStyle.label("Đèn đỏ (giây)")); form.add(redSpinner);
        UiStyle.field(greenSpinner); UiStyle.field(yellowSpinner); UiStyle.field(redSpinner);

        JButton ok = new JButton("Áp dụng");
        JButton cancel = new JButton("Hủy");
        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());
        UiStyle.dialog(this, "Cài đặt thời gian đèn", form, UiStyle.footer(ok, cancel), owner);
        getRootPane().setDefaultButton(ok);
    }

    private void onOk() {
        int green = ((Number) greenSpinner.getValue()).intValue();
        int yellow = ((Number) yellowSpinner.getValue()).intValue();
        int red = ((Number) redSpinner.getValue()).intValue();
        try {
            controller.setDurations(green, yellow, red);
            confirmed = true;
            dispose();
        } catch (InvalidSimulationDataException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
