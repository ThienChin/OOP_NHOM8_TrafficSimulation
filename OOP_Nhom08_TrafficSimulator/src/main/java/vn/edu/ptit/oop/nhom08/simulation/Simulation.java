package vn.edu.ptit.oop.nhom08.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;
import javax.swing.Timer;

import vn.edu.ptit.oop.nhom08.controller.LightController;
import vn.edu.ptit.oop.nhom08.controller.TrafficLightController;
import vn.edu.ptit.oop.nhom08.model.Direction;
import vn.edu.ptit.oop.nhom08.model.LightState;
import vn.edu.ptit.oop.nhom08.model.Vehicle;
import vn.edu.ptit.oop.nhom08.model.VehiclePath;

public class Simulation {
    private List<Vehicle> vehicles;
    private LightController lightController;
    private Timer timer;
    private Runnable onUpdate;
    private long simulationTime = 0;
    private int passedVehiclesCount = 0;
    private final Map<Vehicle, Integer> spawnSlots = new IdentityHashMap<>();
    private static final int GAP = 18;
    // Mỗi 60 ms: xe thường 1 pixel, cứu thương 2 pixel.
    private static final int TICK_MS = 16;
    private static final double DT = TICK_MS / 1000.0;
    // Tốc độ là pixel/tick, không phải km/h. Giữ tốc độ tối đa tương đương bản cũ.
    private final Map<Vehicle, Double> actualSpeeds = new IdentityHashMap<>();
    private final Map<Vehicle, Double> precisePositions = new IdentityHashMap<>();
    private static final double ACCELERATION = 650.0; // pixel/s²
    private static final double BRAKING = 950.0; // pixel/s²
    private static final double SAFE_TIME_GAP = 0.42; // giây
    private static final double LOOK_AHEAD = 100.0; // pixel


    public Simulation() {
        vehicles = new ArrayList<>();
        lightController = new TrafficLightController();
        // Vòng lặp cập nhật mô phỏng chạy mỗi 30ms (~33 fps)
        timer = new Timer(TICK_MS, e -> update());
    }

    public boolean isRunning() {
        return timer != null && timer.isRunning();
    }

    public void start() {
        if (timer != null && !timer.isRunning()) {
            timer.start();
        }
        if (lightController != null) {
            lightController.start(); // Bắt đầu đếm thời gian đèn
        }
    }

    public void pause() {
        if (timer != null && timer.isRunning()) {
            timer.stop();
        }
        if (lightController != null) {
            lightController.pause(); // Tạm dừng đếm thời gian đèn
        }
    }

    public void reset() {
        pause();
        vehicles.clear();
        spawnSlots.clear();
        actualSpeeds.clear();
        precisePositions.clear();
        if (lightController != null) {
            lightController.reset(); // Đặt lại chu kỳ đèn về trạng thái ban đầu
        }
        simulationTime = 0;
        passedVehiclesCount = 0;
        if (onUpdate != null) onUpdate.run();
    }

    // ID không được trùng trên tất cả phương tiện đang có trong mô phỏng.
    public boolean isVehicleIdExists(String id, Direction direction) {
        for (Vehicle other : vehicles) {
            if (other.getDirection() == direction
                    && other.getId().equalsIgnoreCase(id.trim())) {
                return true;
            }
        }
        return false;
    }

    // Vị trí nhập là số thứ tự logic, không phải tọa độ pixel.
    // Cùng số vị trí vẫn được phép ở loại xe khác hoặc hướng khác.
    public boolean isSpawnPositionExists(Class<?> type, Direction direction, int slot) {
        for (Map.Entry<Vehicle, Integer> entry : spawnSlots.entrySet()) {
            Vehicle other = entry.getKey();
            if (other.getClass() == type && other.getDirection() == direction
                    && entry.getValue() == slot) return true;
        }
        return false;
    }

    public void addVehicle(Vehicle v) {
        if (v == null) throw new IllegalArgumentException("Xe không hợp lệ!");
        if (isVehicleIdExists(v.getId(), v.getDirection())) {
            throw new IllegalArgumentException(
                "Mã xe đã tồn tại trong hướng " + v.getDirection() + "!"
            );
        }
        int slot = v.getPosition();
        if (isSpawnPositionExists(v.getClass(), v.getDirection(), slot))
            throw new IllegalArgumentException("Vị trí đã được dùng cho loại xe và hướng này!");

        // Chọn làn: trái 1, thẳng 2/3, phải 4.
        int lane = VehiclePath.assignedLane(v);
        if (v.getDestination() == v.getDirection()) {
            int n1=0,n2=0;
            for(Vehicle other:vehicles) if(other.getDirection()==v.getDirection() && other.getDestination()==v.getDirection()) {
                if(other.getLane()==1)n1++; else if(other.getLane()==2)n2++;
            }
            lane=n1<=n2?1:2;
        }
        v.setLane(lane);
        // Xếp cuối hàng trong đúng làn, không khóa cả hướng.
        int start = 0;
        for (Vehicle other : vehicles) {
            if (other.getDirection() == v.getDirection() && other.getLane()==v.getLane()) {
                start = Math.min(start, other.getPosition() - getVehicleLength(v) - GAP);
            }
        }
        v.setPosition(start);
        vehicles.add(v);
        spawnSlots.put(v, slot);
        actualSpeeds.put(v, 0.0);
        precisePositions.put(v, (double) start);
        if (onUpdate != null) onUpdate.run();
    }

    public List<Vehicle> getVehicles() { return Collections.unmodifiableList(vehicles); }
    public LightController getTrafficLightController() { return lightController; }
    public void setOnUpdate(Runnable onUpdate) { this.onUpdate = onUpdate; }
    public long getSimulationTime() { return simulationTime * TICK_MS / 1000; }
    public double getRenderPosition(Vehicle v) { return precisePositions.getOrDefault(v, (double)v.getPosition()); }
    public int getPassedVehiclesCount() { return passedVehiclesCount; }

    // Khoảng cách tâm xe tối thiểu khi chạy qua vùng giao nhau / nhập làn.
    private static final double COLLISION_RADIUS = 24.0;
    private static final int MERGE_GAP = 34;
    private static final int APPROACH_WINDOW = 65;

    private void update() {
        simulationTime++;
        List<Vehicle> toRemove = new ArrayList<>();
        // Duyệt xe đã vào nút giao trước, sau đó xe gần vạch dừng hơn.
        // Việc cấp quyền nhất quán tránh hai xe cùng "nhìn thấy đường trống".
        List<Vehicle> order = new ArrayList<>(vehicles);
        order.sort((a, b) -> {
            if (a.hasEnteredIntersection() != b.hasEnteredIntersection())
                return a.hasEnteredIntersection() ? -1 : 1;
            int da = (int)Math.round(VehiclePath.entry(a.getDirection())) - a.getPosition();
            int db = (int)Math.round(VehiclePath.entry(b.getDirection())) - b.getPosition();
            int c = Integer.compare(da, db);
            if (c != 0) return c;
            return Integer.compare(vehicles.indexOf(a), vehicles.indexOf(b));
        });
        Map<Vehicle, Double> approved = new IdentityHashMap<>();
        Map<Vehicle, Double> plannedPrecise = new IdentityHashMap<>();
        for (Vehicle v : order) {
            int current = v.getPosition();
            double currentPrecise = Math.max(current, precisePositions.getOrDefault(v, (double) current));
            double maxSpeed = getMaxSpeed(v);
            double allowed = maxSpeed;
            int stop = getStopPosition(v);
            boolean beforeStop = current <= stop;
            if (beforeStop && (lightController.getState(v.getDirection()) != LightState.GREEN
                    || !canEnterIntersection(v))) {
                allowed = Math.min(allowed, speedForDistance(stop - currentPrecise));
            }
            // Xe cùng làn: giảm tốc trước khi đến đuôi xe phía trước.
            for (Vehicle other : vehicles) {
                if (other == v || other.getDirection() != v.getDirection() || other.getLane() != v.getLane()) continue;
                if (current < VehiclePath.entry(v.getDirection()) && other.getPosition() > current
                        && other.getPosition() < VehiclePath.entry(other.getDirection()) + 2) {
                    double space = other.getPosition() - currentPrecise - getVehicleLength(v) - GAP;
                    allowed = Math.min(allowed, speedForDistance(space));
                }
            }
            double oldSpeed = actualSpeeds.getOrDefault(v, 0.0);
            double newSpeed = oldSpeed + Math.max(-BRAKING * DT, Math.min(ACCELERATION * DT, allowed - oldSpeed));
            newSpeed = Math.max(0.0, Math.min(maxSpeed, newSpeed));
            double targetPrecise = currentPrecise + newSpeed * DT;
            int next = (int)Math.floor(targetPrecise + 1e-7);
            if (beforeStop && (lightController.getState(v.getDirection()) != LightState.GREEN
                    || !canEnterIntersection(v))) next = Math.min(next, stop);
            if (targetPrecise > currentPrecise && !isSafeNextPosition(v, currentPrecise, targetPrecise, approved)) {
                next = current;
                targetPrecise = currentPrecise;
                newSpeed = 0.0;
            }
            // Không tích lũy quãng đường vượt quá vạch dừng hoặc xe trước.
            if (beforeStop && (lightController.getState(v.getDirection()) != LightState.GREEN || !canEnterIntersection(v)) && targetPrecise > stop) {
                targetPrecise = stop;
                newSpeed = 0.0;
            }
            if (next < (int)Math.floor(targetPrecise + 1e-7)) {
                targetPrecise = next;
                newSpeed = 0.0;
            }
            approved.put(v, Math.max(currentPrecise, targetPrecise));
            plannedPrecise.put(v, Math.max(current, targetPrecise));
            actualSpeeds.put(v, newSpeed);
        }
        for (Vehicle v : vehicles) {
            int next = (int)Math.floor(approved.get(v) + 1e-7);
            v.setPosition(next);
            precisePositions.put(v, plannedPrecise.get(v));
            if (next > VehiclePath.entry(v.getDirection()) + 2) v.setIntersectionEntered(true);
            if (next > VehiclePath.exit(v) + 310) {
                toRemove.add(v);
                passedVehiclesCount++;
            }
        }
        vehicles.removeAll(toRemove);
        for (Vehicle v : toRemove) {
            spawnSlots.remove(v);
            actualSpeeds.remove(v);
            precisePositions.remove(v);
        }
        if (onUpdate != null) onUpdate.run();
    }

    // Kiểm tra toàn bộ đoạn di chuyển trong tick, tránh xe chạy nhanh xuyên qua xe khác.
    // Dùng vị trí double để không chặn chuyển động chỉ vì làm tròn lên pixel.
    private boolean isSafeNextPosition(Vehicle v, double from, double next, Map<Vehicle, Double> approved) {
        for (Vehicle other : vehicles) {
            if (other == v) continue;
            double op = approved.getOrDefault(other,
                    precisePositions.getOrDefault(other, (double)other.getPosition()));
            boolean sameEntryLane = v.getDirection() == other.getDirection()
                    && v.getLane() == other.getLane()
                    && from < VehiclePath.entry(v.getDirection())
                    && op < VehiclePath.entry(other.getDirection());
            if (sameEntryLane) {
                if (op >= from && next > op - getVehicleLength(v) - GAP) return false;
                continue;
            }
            double minGap = Math.max(COLLISION_RADIUS,
                    (getVehicleLength(v) + getVehicleLength(other)) * 0.45);
            if (v.getDestination() == other.getDestination()
                    && next >= VehiclePath.exit(v) - 15
                    && op >= VehiclePath.exit(other) - 15)
                minGap = Math.max(minGap, MERGE_GAP);
            java.awt.geom.Point2D.Double otherPoint = VehiclePath.point(other, op);
            // Mẫu cách nhau tối đa 3px để phát hiện va chạm xuyên khung hình.
            int samples = Math.max(1, (int)Math.ceil((next - from) / 3.0));
            for (int i = 1; i <= samples; i++) {
                double progress = from + (next - from) * i / samples;
                double futureGap = VehiclePath.point(v, progress).distance(otherPoint);
                if (futureGap < minGap) {
                    // Nếu hai xe đã quá gần từ trước, cho phép xe đang THOÁT
                    // khỏi vùng chồng lấn tiếp tục đi khi khoảng cách tăng.
                    // Không cho phép xe tiến sâu hơn vào xe khác.
                    double oldGap = VehiclePath.point(v, from).distance(otherPoint);
                    if (oldGap < minGap && futureGap > oldGap + 1e-6) continue;
                    return false;
                }
            }
        }
        return true;
    }

    private int getVehicleLength(Vehicle v) {
        String type = v.getClass().getSimpleName();
        if (type.equals("Bus")) return 50;
        if (type.equals("Ambulance")) return 36;
        if (type.equals("Motorcycle")) return 16;
        return 32;
    }

    private double getMaxSpeed(Vehicle v) {
        return (v instanceof vn.edu.ptit.oop.nhom08.model.Ambulance) ? 450.0 : 300.0; // pixel/giây, tương đương bản 35ms
    }

    // Quãng đường còn ngắn thì tốc độ mục tiêu giảm dần (phanh sớm).
    private double speedForDistance(double distance) {
        if (distance <= 0) return 0;
        return Math.sqrt(2.0 * BRAKING * distance) * 0.90;
    }

    private boolean canEnterIntersection(Vehicle v) {
        if (v.hasEnteredIntersection()) return true;
        double vEntry = VehiclePath.entry(v.getDirection());
        double vDistance = Math.max(0, vEntry - v.getPosition());
        // Chỉ dự đoán khi đang tiếp cận nút giao, tránh khóa đường quá sớm.
        if (vDistance > LOOK_AHEAD) return true;
        for (Vehicle other : vehicles) {
            if (other == v) continue;
            double[] conflict = firstConflictProgress(v, other);
            if (conflict == null) continue;
            double otherEntry = VehiclePath.entry(other.getDirection());
            double otherPos = other.getPosition();
            if (otherPos > VehiclePath.exit(other) + 20) continue;
            // Xe đã ở trong nút giao có quyền đi hết.
            // Chỉ xe thực sự đã đi qua cửa nút giao mới giữ quyền ưu tiên.
            // Xe còn đứng ở vạch dừng không được khóa các hướng khác.
            if (other.hasEnteredIntersection()
                    && otherPos <= VehiclePath.exit(other) + 20) {
                // Không khóa khi xe kia đã đi qua điểm xung đột trên quỹ đạo.
                double[] crossing = firstConflictProgress(v, other);
                if (crossing != null && otherPos <= crossing[1] + 28) return false;
                continue;
            }
            if (otherPos < otherEntry - LOOK_AHEAD) continue;
            if (lightController.getState(other.getDirection()) != LightState.GREEN) continue;
            // Ước lượng thời gian đến vùng giao cắt dựa trên quỹ đạo, không chỉ hướng.
            double vSpeed = Math.max(5.0, actualSpeeds.getOrDefault(v, getMaxSpeed(v)));
            double oSpeed = Math.max(5.0, actualSpeeds.getOrDefault(other, getMaxSpeed(other)));
            double etaV = Math.max(0, conflict[0] - v.getPosition()) / vSpeed;
            double etaO = Math.max(0, conflict[1] - otherPos) / oSpeed;
            // Chỉ nhường nếu thời gian đến điểm xung đột gần nhau.
            if (Math.abs(etaV - etaO) < SAFE_TIME_GAP) {
                // Xe rẽ trái nhường xe đối diện đi thẳng; trường hợp còn lại dùng thứ tự đến.
                if (isLeftTurn(v) && !other.isTurning() && other.getDirection() == opposite(v.getDirection())) return false;
                if (etaO + 0.05 < etaV) return false;
                if (Math.abs(etaV - etaO) <= 0.05 && vehicles.indexOf(v) > vehicles.indexOf(other)) return false;
            }
        }
        return true;
    }

    private double[] firstConflictProgress(Vehicle a, Vehicle b) {
        double startA = VehiclePath.entry(a.getDirection());
        double startB = VehiclePath.entry(b.getDirection());
        double best = Double.POSITIVE_INFINITY;
        double[] result = null;
        for (double x = startA; x <= VehiclePath.exit(a); x += 8) {
            java.awt.geom.Point2D.Double pa = VehiclePath.point(a, x);
            for (double y = startB; y <= VehiclePath.exit(b); y += 8) {
                if (pa.distance(VehiclePath.point(b, y)) < COLLISION_RADIUS) {
                    double score = x - startA + y - startB;
                    if (score < best) { best = score; result = new double[] {x, y}; }
                }
            }
        }
        return result;
    }

    private boolean pathsConflict(Vehicle a, Vehicle b) {
        // So sánh hình học hai quỹ đạo ở nút giao; hai đường không cắt nhau không khóa nhau.
        double ea = VehiclePath.entry(a.getDirection());
        double eb = VehiclePath.entry(b.getDirection());
        for (double x = ea; x <= VehiclePath.exit(a); x += 6) {
            java.awt.geom.Point2D.Double pa = VehiclePath.point(a, x);
            for (double y = eb; y <= VehiclePath.exit(b); y += 6) {
                java.awt.geom.Point2D.Double pb = VehiclePath.point(b, y);
                if (pa.distance(pb) < COLLISION_RADIUS) return true;
            }
        }
        return false;
    }

    private Direction opposite(Direction d) {
        switch(d) {
            case EAST: return Direction.WEST;
            case WEST: return Direction.EAST;
            case NORTH: return Direction.SOUTH;
            default: return Direction.NORTH;
        }
    }
    private boolean isLeftTurn(Vehicle v) {
        switch(v.getDirection()) {
            case EAST: return v.getDestination() == Direction.NORTH;
            case WEST: return v.getDestination() == Direction.SOUTH;
            case NORTH: return v.getDestination() == Direction.WEST;
            default: return v.getDestination() == Direction.EAST;
        }
    }
    private int getStopPosition(Vehicle v) {
        return (int)VehiclePath.entry(v.getDirection()) - getVehicleLength(v)/2 - 5;
    }
}
