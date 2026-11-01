package vn.edu.ptit.oop.nhom08.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;
import vn.edu.ptit.oop.nhom08.model.Direction;
import vn.edu.ptit.oop.nhom08.model.Vehicle;
import vn.edu.ptit.oop.nhom08.model.VehicleFactory;
import vn.edu.ptit.oop.nhom08.simulation.Simulation;

/**
 * Form nhập liệu (JDialog modal) để thêm phương tiện.
 * Dữ liệu sai làm VehicleFactory ném InvalidSimulationDataException; dialog bắt
 * lại, báo lỗi và vẫn mở để người dùng sửa.
 */
public class AddVehicleDialog extends JDialog {
    private static final long serialVersionUID = 1L;

    private final JComboBox<String> typeBox = new JComboBox<>(VehicleFactory.TYPES);
    private final JTextField idField = new JTextField(10);
    private final JTextField positionField = new JTextField("1", 10);
    private final JComboBox<String> directionBox = new JComboBox<>(new String[]{"Phía Tây", "Phía Đông", "Phía Bắc", "Phía Nam"});

    private final JComboBox<String> destinationBox = new JComboBox<>(new String[]{"Ngẫu nhiên", "Đông", "Tây", "Bắc", "Nam"});
    private Vehicle result;
    private final Simulation simulation;

    public AddVehicleDialog(Frame owner, Simulation simulation) {
        super(owner, "Thêm phương tiện", true);
        this.simulation = simulation;
        directionBox.setSelectedItem("Phía Tây");
        destinationBox.setSelectedItem("Đông");

        JPanel form = UiStyle.form(5);
        form.add(UiStyle.label("Loại phương tiện")); form.add(typeBox);
        form.add(UiStyle.label("Mã phương tiện")); form.add(idField);
        form.add(UiStyle.label("Vị trí xuất phát")); form.add(directionBox);
        form.add(UiStyle.label("Số thứ tự vị trí")); form.add(positionField);
        form.add(UiStyle.label("Hướng đích")); form.add(destinationBox);
        UiStyle.field(typeBox); UiStyle.field(idField); UiStyle.field(directionBox);
        UiStyle.field(positionField); UiStyle.field(destinationBox);

        JButton ok = new JButton("Thêm phương tiện");
        JButton cancel = new JButton("Hủy");
        ok.addActionListener(e -> onOk());
        cancel.addActionListener(e -> dispose());
        UiStyle.dialog(this, "Thêm phương tiện mới", form, UiStyle.footer(ok, cancel), owner);
        getRootPane().setDefaultButton(ok);
    }

    private void onOk() {
        try {
            result = VehicleFactory.createFromText(
                    (String) typeBox.getSelectedItem(),
                    idField.getText(),
                    positionField.getText(),
                    movementFromOrigin());
            Direction destination = parseDestination();
            if (destination == opposite(result.getDirection())) {
                throw new InvalidSimulationDataException("destination", "Không hỗ trợ quay đầu 180 độ!");
            }
            result.setDestination(destination);
            if (simulation.isVehicleIdExists(
                    result.getId(),
                    result.getDirection())) {

                result = null;

                JOptionPane.showMessageDialog(
                    this,
                    "Mã xe đã tồn tại trong hướng đã chọn! Vui lòng nhập mã khác.",
                    "Trùng mã xe",
                    JOptionPane.WARNING_MESSAGE
                );

                idField.requestFocusInWindow();
                return;
            }
            int slot = Integer.parseInt(positionField.getText().trim());
            if (simulation.isSpawnPositionExists(result.getClass(), result.getDirection(), slot)) {
                result = null;
                JOptionPane.showMessageDialog(this,
                        "Vị trí này đã được dùng cho cùng loại xe và cùng hướng!",
                        "Trùng vị trí", JOptionPane.WARNING_MESSAGE);
                positionField.requestFocusInWindow();
                return;
            }
            dispose();
        } catch (InvalidSimulationDataException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Dữ liệu không hợp lệ",
                    JOptionPane.WARNING_MESSAGE);
        }
    }

    /** Hướng trong model là hướng xe ĐANG ĐI, không phải phía xe xuất phát. */
    private Direction movementFromOrigin() {
        String origin = (String) directionBox.getSelectedItem();
        if ("Phía Tây".equals(origin)) return Direction.EAST;
        if ("Phía Đông".equals(origin)) return Direction.WEST;
        if ("Phía Bắc".equals(origin)) return Direction.SOUTH;
        return Direction.NORTH;
    }

    private Direction opposite(Direction d) {
        switch(d) {
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            case NORTH: return Direction.SOUTH;
            default: return Direction.NORTH;
        }
    }
    private Direction parseDestination() {
        String selected = (String) destinationBox.getSelectedItem();
        if ("Ngẫu nhiên".equals(selected)) {
            Direction[] values = Direction.values();
            Direction from = movementFromOrigin();
            Direction chosen;
            do { chosen = values[java.util.concurrent.ThreadLocalRandom.current().nextInt(values.length)]; }
            while (chosen == opposite(from));
            return chosen;
        }
        switch(selected) {
            case "Đông": return Direction.EAST;
            case "Tây": return Direction.WEST;
            case "Bắc": return Direction.NORTH;
            default: return Direction.SOUTH;
        }
    }

    public boolean isConfirmed() {
        return result != null;
    }

    /** Phương tiện đã tạo, hoặc null nếu người dùng hủy. */
    public Vehicle getResult() {
        return result;
    }

    /** Tên loại đang chọn, dùng cho thông báo trạng thái. */
    public String getSelectedType() {
        return (String) typeBox.getSelectedItem();
    }
}
