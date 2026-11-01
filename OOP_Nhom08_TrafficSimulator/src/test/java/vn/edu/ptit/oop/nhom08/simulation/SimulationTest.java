package vn.edu.ptit.oop.nhom08.simulation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import vn.edu.ptit.oop.nhom08.model.Car;
import vn.edu.ptit.oop.nhom08.model.Direction;

class SimulationTest {

    @Test
    void getVehiclesIsReadOnly() {
        Simulation sim = new Simulation();
        assertThrows(UnsupportedOperationException.class,
                () -> sim.getVehicles().add(new Car("C1", 2, Direction.EAST, 0)));
    }

    @Test
    void addVehicleMovesNewCarBehindOverlappingCarInSameDirection() {
        Simulation sim = new Simulation();
        Car first = new Car("C1", 2, Direction.EAST, 100);
        Car second = new Car("C2", 2, Direction.EAST, 100);
        sim.addVehicle(first);
        sim.addVehicle(second);
        assertEquals(100, first.getPosition());
        assertEquals(100 - 32 - 15, second.getPosition()); // dài 32 + khoảng hở 15
        assertEquals(2, sim.getVehicles().size());
    }

    @Test
    void addVehicleDoesNotMoveCarsInDifferentDirections() {
        Simulation sim = new Simulation();
        sim.addVehicle(new Car("C1", 2, Direction.EAST, 100));
        Car other = new Car("C2", 2, Direction.NORTH, 100);
        sim.addVehicle(other);
        assertEquals(100, other.getPosition());
    }

    @Test
    void resetClearsVehiclesAndCounters() {
        Simulation sim = new Simulation();
        sim.addVehicle(new Car("C1", 2, Direction.EAST, 100));
        sim.reset();
        assertTrue(sim.getVehicles().isEmpty());
        assertEquals(0, sim.getPassedVehiclesCount());
        assertEquals(0, sim.getSimulationTime());
    }
}
