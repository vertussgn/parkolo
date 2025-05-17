package com.example.parkolo;

import com.example.parkolo.entity.ParkingEvent;
import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.repository.ParkingEventRepository;
import com.example.parkolo.repository.ParkingSpotRepository;
import com.example.parkolo.service.ParkingSpotService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ParkingSpotServiceTest {

    private ParkingSpotRepository parkingSpotRepository;
    private ParkingEventRepository parkingEventRepository;
    private ParkingSpotService service;
    private static final int FREE_SPOT_NUMBER = 3;

    @BeforeEach
    void setUp() {
        parkingSpotRepository = mock(ParkingSpotRepository.class);
        parkingEventRepository = mock(ParkingEventRepository.class);
        service
                = new ParkingSpotService(
                        parkingSpotRepository, parkingEventRepository);
    }

    @Test
    void testAddCarToFreeSpot() {
        ParkingSpot spot = new ParkingSpot(1);
        when(parkingSpotRepository.findBySpotNumber(1))
                .thenReturn(Optional.of(spot));
        when(parkingSpotRepository
                .save(any(ParkingSpot.class)))
                .thenAnswer(i -> i.getArgument(0));

        ParkingSpot result = service.addCar(1, "ABC-123", "SUV", "Blue");

        assertTrue(result.getOccupied());
        assertEquals("ABC-123", result.getLicensePlate());
        verify(parkingEventRepository).save(any(ParkingEvent.class));
    }

    @Test
    void testAddCarToOccupiedSpotThrowsException() {
        ParkingSpot spot = new ParkingSpot(1);
        spot.setOccupied(true);
        when(parkingSpotRepository.findBySpotNumber(1))
                .thenReturn(Optional.of(spot));

        assertThrows(IllegalStateException.class, () ->
                service.addCar(1, "XYZ-999", "Sedan", "Red"));
    }


    @Test
    void testGetOccupiedParkingSpots() {
        List<ParkingSpot> spots
                = Arrays.asList(new ParkingSpot(1),
                new ParkingSpot(2));
        when(parkingSpotRepository.findByOccupied(true)).thenReturn(spots);

        List<ParkingSpot> result = service.getOccupiedParkingSpots();

        assertEquals(2, result.size());
        verify(parkingSpotRepository).findByOccupied(true);
    }

    @Test
    void testGetFreeParkingSpots() {
        List<ParkingSpot> spots = Arrays.asList(
                new ParkingSpot(FREE_SPOT_NUMBER));
        when(parkingSpotRepository.findByOccupied(false)).thenReturn(spots);

        List<ParkingSpot> result = service.getFreeParkingSpots();

        assertEquals(1, result.size());
        verify(parkingSpotRepository).findByOccupied(false);
    }
}
