package com.example.parkolo;

import com.example.parkolo.entity.ParkingSpot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ParkingSpotTest {

    private static final long TEST_ID = 100L;
    private static final int SLEEP_TIME_MS = 1000;
    private ParkingSpot spot;

    @BeforeEach
    void setUp() {
        spot = new ParkingSpot(1);
    }

    @Test
    void testDefaultConstructor() {
        ParkingSpot defaultSpot = new ParkingSpot();
        assertFalse(defaultSpot.getOccupied());
        assertNotNull(defaultSpot.getLastUpdated());
    }

    @Test
    void testParameterizedConstructor() {
        assertEquals(1, spot.getSpotNumber());
        assertFalse(spot.getOccupied());
        assertNotNull(spot.getLastUpdated());
    }

    @Test
    void testSettersAndGetters() {
        spot.setId(TEST_ID);  // Konstans használata
        spot.setLicensePlate("ABC-123");
        spot.setCarType("SUV");
        spot.setColor("Red");
        spot.setOccupied(true);

        assertEquals(TEST_ID, spot.getId());
        assertEquals("ABC-123", spot.getLicensePlate());
        assertEquals("SUV", spot.getCarType());
        assertEquals("Red", spot.getColor());
        assertTrue(spot.getOccupied());
    }

    @Test
    void testLastUpdatedChangesOnSet() {
        LocalDateTime beforeUpdate = spot.getLastUpdated();

        try {
            Thread.sleep(SLEEP_TIME_MS); // Konstans használata
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        spot.setColor("Blue");
        LocalDateTime afterUpdate = spot.getLastUpdated();

        assertTrue(afterUpdate.isAfter(beforeUpdate));
    }

    @Test
    void testToString() {
        spot.setId(1L);
        spot.setLicensePlate("XYZ-789");
        spot.setCarType("Hatchback");
        spot.setColor("Green");
        spot.setOccupied(true);

        String str = spot.toString();
        assertTrue(str.contains("spotNumber=1"));
        assertTrue(str.contains("licensePlate='XYZ-789'"));
        assertTrue(str.contains("carType='Hatchback'"));
        assertTrue(str.contains("color='Green'"));
        assertTrue(str.contains("occupied=true"));
    }
}