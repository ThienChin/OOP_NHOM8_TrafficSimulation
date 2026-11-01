package vn.edu.ptit.oop.nhom08.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;
import vn.edu.ptit.oop.nhom08.model.Direction;
import vn.edu.ptit.oop.nhom08.model.LightState;

class TrafficLightControllerTest {

    @Test
    void initialStateEastWestGreenNorthSouthRed() {
        LightController c = new TrafficLightController();
        assertEquals(LightState.GREEN, c.getState(Direction.EAST));
        assertEquals(LightState.GREEN, c.getState(Direction.WEST));
        assertEquals(LightState.RED, c.getState(Direction.NORTH));
        assertEquals(LightState.RED, c.getState(Direction.SOUTH));
    }

    @Test
    void setDurationsUpdatesValuesAndCountdown() throws Exception {
        LightController c = new TrafficLightController();
        c.setDurations(10, 2, 12);
        assertEquals(10, c.getGreenDuration());
        assertEquals(2, c.getYellowDuration());
        assertEquals(12, c.getRedDuration());
        assertEquals(10, c.getRemainingSeconds(Direction.EAST));  // đang xanh
        assertEquals(12, c.getRemainingSeconds(Direction.NORTH)); // đang đỏ
    }

    @Test
    void setDurationsRejectsNonPositiveAndKeepsOldValues() {
        LightController c = new TrafficLightController();
        assertThrows(InvalidSimulationDataException.class, () -> c.setDurations(0, 3, 18));
        assertThrows(InvalidSimulationDataException.class, () -> c.setDurations(15, -1, 18));
        assertThrows(InvalidSimulationDataException.class, () -> c.setDurations(15, 3, 0));
        assertEquals(15, c.getGreenDuration());
        assertEquals(3, c.getYellowDuration());
        assertEquals(18, c.getRedDuration());
    }

    @Test
    void resetRestoresInitialState() {
        LightController c = new TrafficLightController();
        c.reset();
        assertEquals(LightState.GREEN, c.getState(Direction.EAST));
        assertEquals(LightState.RED, c.getState(Direction.NORTH));
        assertEquals(15, c.getRemainingSeconds(Direction.EAST));
        assertEquals(18, c.getRemainingSeconds(Direction.NORTH));
    }
}
