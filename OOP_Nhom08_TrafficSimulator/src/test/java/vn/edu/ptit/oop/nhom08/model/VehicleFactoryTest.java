package vn.edu.ptit.oop.nhom08.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import vn.edu.ptit.oop.nhom08.exception.InvalidSimulationDataException;

class VehicleFactoryTest {

    @Test
    void createsCorrectSubclassForEachType() throws Exception {
        assertEquals(Car.class,
                VehicleFactory.create(VehicleFactory.CAR,
                        "C1", 2, Direction.EAST, 0).getClass());

        assertEquals(Bus.class,
                VehicleFactory.create(VehicleFactory.BUS,
                        "B1", 2, Direction.EAST, 0).getClass());

        assertEquals(Motorcycle.class,
                VehicleFactory.create(VehicleFactory.MOTORCYCLE,
                        "M1", 2, Direction.EAST, 0).getClass());

        assertEquals(Ambulance.class,
                VehicleFactory.create(VehicleFactory.AMBULANCE,
                        "A1", 2, Direction.EAST, 0).getClass());
    }

    @Test
    void speedIsNotMultipliedBySubclass() throws Exception {
        assertEquals(2.0,
                VehicleFactory.create(VehicleFactory.CAR,
                        "C1", 2, Direction.NORTH, 0).getSpeed(), 1e-9);

        assertEquals(2.0,
                VehicleFactory.create(VehicleFactory.BUS,
                        "B1", 2, Direction.NORTH, 0).getSpeed(), 1e-9);

        assertEquals(2.0,
                VehicleFactory.create(VehicleFactory.AMBULANCE,
                        "A1", 2, Direction.NORTH, 0).getSpeed(), 1e-9);
    }

    @Test
    void trimsId() throws Exception {
        assertEquals("X9",
                VehicleFactory.create(VehicleFactory.CAR,
                        "  X9  ", 1, Direction.WEST, 5).getId());
    }

    @Test
    void rejectsBlankOrNegativeId() {
        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        VehicleFactory.CAR, "   ", 2, Direction.EAST, 0));

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        VehicleFactory.CAR, "-5", 2, Direction.EAST, 0));
    }

    @Test
    void rejectsNonPositiveSpeedAndNegativePosition() {
        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        VehicleFactory.CAR, "C1", 0, Direction.EAST, 0));

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        VehicleFactory.CAR, "C1", -1, Direction.EAST, 0));

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        VehicleFactory.CAR, "C1", 2, Direction.EAST, -1));
    }

    @Test
    void rejectsUnknownTypeAndNullDirection() {
        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        "Xe tăng", "T1", 2, Direction.EAST, 0));

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.create(
                        VehicleFactory.CAR, "C1", 2, null, 0));
    }

    @Test
    void rejectsNonNumericText() {

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.createFromText(
                        VehicleFactory.CAR,
                        "C1",
                        "abc",
                        Direction.EAST));

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.createFromText(
                        VehicleFactory.CAR,
                        "C1",
                        "1.5",
                        Direction.EAST));

        assertThrows(InvalidSimulationDataException.class,
                () -> VehicleFactory.createFromText(
                        VehicleFactory.CAR,
                        "C1",
                        null,
                        Direction.EAST));
    }
}
