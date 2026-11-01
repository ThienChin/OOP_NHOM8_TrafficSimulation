package vn.edu.ptit.oop.nhom08.model;

import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;

/**
 * Mẫu Factory: tạo đúng lớp con của Vehicle theo tên loại và kiểm tra dữ liệu.
 * Giao diện chỉ làm việc với kiểu cha Vehicle (đa hình).
 */
public final class VehicleFactory {
    public static final String CAR = "Ô tô";
    public static final String MOTORCYCLE = "Xe máy";
    public static final String BUS = "Xe buýt";
    public static final String AMBULANCE = "Xe cứu thương";

    public static final String[] TYPES = {CAR, MOTORCYCLE, BUS, AMBULANCE};

    private VehicleFactory() {
    }

    public static Vehicle createFromText(String type, String id,
            String positionText, Direction direction) throws InvalidSimulationDataException {
        int position;
        try {
            position = Integer.parseInt(positionText == null ? "" : positionText.trim());
        } catch (NumberFormatException e) {
            throw new InvalidSimulationDataException("position", "Vị trí phải là số nguyên hợp lệ!");
        }
        double speed = AMBULANCE.equals(type) ? 75.0 : 20.0;
        return create(type, id, speed, direction, position);
    }

    public static Vehicle create(String type, String id, double speed, Direction direction,
            int position) throws InvalidSimulationDataException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidSimulationDataException("id", "Mã phương tiện không được để trống!");
        }
        String trimmedId = id.trim();
        if (trimmedId.startsWith("-")) {
            throw new InvalidSimulationDataException("id", "Mã phương tiện không được mang giá trị âm!");
        }
        if (type == null) {
            throw new InvalidSimulationDataException("type", "Vui lòng chọn loại phương tiện!");
        }
        if (direction == null) {
            throw new InvalidSimulationDataException("direction", "Vui lòng chọn hướng di chuyển!");
        }
        if (Double.isNaN(speed) || Double.isInfinite(speed) || speed <= 0) {
            throw new InvalidSimulationDataException("speed", "Tốc độ phải lớn hơn 0!");
        }
        if (position < 0) {
            throw new InvalidSimulationDataException("position", "Vị trí không được âm!");
        }

        switch (type) {
            case CAR:
                return new Car(trimmedId, speed, direction, position);
            case MOTORCYCLE:
                return new Motorcycle(trimmedId, speed, direction, position);
            case BUS:
                return new Bus(trimmedId, speed, direction, position);
            case AMBULANCE:
                return new Ambulance(trimmedId, speed, direction, position);
            default:
                throw new InvalidSimulationDataException("type", "Loại phương tiện không hợp lệ: " + type);
        }
    }
}
