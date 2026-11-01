package vn.edu.ptit.oop.nhom08.controller;

import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;
import vn.edu.ptit.oop.nhom08.model.Direction;
import vn.edu.ptit.oop.nhom08.model.LightState;

/**
 * Interface tự định nghĩa: hợp đồng điều khiển đèn giao thông.
 * Simulation chỉ phụ thuộc vào interface này nên có thể thay bằng chiến lược
 * khác (ví dụ AdaptiveController - BONUS) mà không sửa Simulation.
 */
public interface LightController {
    void start();

    void pause();

    void reset();

    LightState getState(Direction direction);

    int getRemainingSeconds(Direction direction);

    int getGreenDuration();

    int getYellowDuration();

    int getRedDuration();

    /**
     * @throws InvalidSimulationDataException nếu một thời lượng không dương
     */
    void setDurations(int green, int yellow, int red) throws InvalidSimulationDataException;
}
