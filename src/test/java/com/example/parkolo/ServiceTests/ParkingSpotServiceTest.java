package com.example.parkolo.ServiceTests;

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
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.never;

class ParkingSpotServiceTest {

    private static final int FREE_SPOT_NUMBER = 3;
    private static final int INITIAL_SPOTS_COUNT = 20;
    private static final long NON_EXISTENT_ID = 999L;
    private static final int NON_EXISTENT_SPOT_NUMBER = 999;
    private static final long VALID_ID = 1L;
    private static final int SPOT_NUMBER_1 = 1;
    private static final int SPOT_NUMBER_2 = 2;
    private static final int TEST_SPOT_NUMBER = 5;

    private ParkingSpotRepository parkingSpotRepository;
    private ParkingEventRepository parkingEventRepository;
    private ParkingSpotService service;

    @BeforeEach
    void setUp() {
        parkingSpotRepository = mock(ParkingSpotRepository.class);
        parkingEventRepository = mock(ParkingEventRepository.class);
        service = new ParkingSpotService(parkingSpotRepository, parkingEventRepository);
    }

    @Test
    void testAddCarToFreeSpot() {
        ParkingSpot spot = new ParkingSpot(SPOT_NUMBER_1);
        when(parkingSpotRepository.findBySpotNumber(SPOT_NUMBER_1)).thenReturn(Optional.of(spot));
        when(parkingSpotRepository.save(any(ParkingSpot.class))).thenAnswer(i -> i.getArgument(0));

        ParkingSpot result = service.addCar(SPOT_NUMBER_1, "ABC-123", "SUV", "Blue");

        assertTrue(result.getOccupied());
        assertEquals("ABC-123", result.getLicensePlate());
        verify(parkingEventRepository).save(any(ParkingEvent.class));
    }

    @Test
    void testAddCarToOccupiedSpotThrowsException() {
        ParkingSpot spot = new ParkingSpot(SPOT_NUMBER_1);
        spot.setOccupied(true);
        when(parkingSpotRepository.findBySpotNumber(SPOT_NUMBER_1)).thenReturn(Optional.of(spot));

        assertThrows(IllegalStateException.class, () ->
                service.addCar(SPOT_NUMBER_1, "XYZ-999", "Sedan", "Red"));
    }

    @Test
    void testGetOccupiedParkingSpots() {
        List<ParkingSpot> spots = Arrays.asList(new ParkingSpot(SPOT_NUMBER_1), new ParkingSpot(SPOT_NUMBER_2));
        when(parkingSpotRepository.findByOccupied(true)).thenReturn(spots);

        List<ParkingSpot> result = service.getOccupiedParkingSpots();

        assertEquals(2, result.size());
        verify(parkingSpotRepository).findByOccupied(true);
    }

    @Test
    void testGetFreeParkingSpots() {
        List<ParkingSpot> spots = Arrays.asList(new ParkingSpot(FREE_SPOT_NUMBER));
        when(parkingSpotRepository.findByOccupied(false)).thenReturn(spots);

        List<ParkingSpot> result = service.getFreeParkingSpots();

        assertEquals(1, result.size());
        verify(parkingSpotRepository).findByOccupied(false);
    }

    @Test
    void initializeParkingSpotsShouldCreate20SpotsWhenEmpty() {
        when(parkingSpotRepository.count()).thenReturn(0L);
        service.initializeParkingSpots();
        verify(parkingSpotRepository, times(INITIAL_SPOTS_COUNT)).save(any(ParkingSpot.class));
    }

    @Test
    void initializeParkingSpotsShouldDoNothingWhenAlreadyInitialized() {
        when(parkingSpotRepository.count()).thenReturn((long) INITIAL_SPOTS_COUNT);
        service.initializeParkingSpots();
        verify(parkingSpotRepository, never()).save(any(ParkingSpot.class));
    }

    @Test
    void getAllParkingSpotsShouldReturnOrderedList() {
        ParkingSpot spot1 = new ParkingSpot(SPOT_NUMBER_1);
        ParkingSpot spot2 = new ParkingSpot(SPOT_NUMBER_2);
        List<ParkingSpot> mockSpots = Arrays.asList(spot1, spot2);
        when(parkingSpotRepository.findAllOrderBySpotNumber()).thenReturn(mockSpots);

        List<ParkingSpot> result = service.getAllParkingSpots();
        assertEquals(SPOT_NUMBER_1, result.get(0).getSpotNumber());
        assertEquals(SPOT_NUMBER_2, result.get(1).getSpotNumber());
    }

    @Test
    void getParkingSpotByIdShouldReturnSpotWhenExists() {
        ParkingSpot expected = new ParkingSpot(TEST_SPOT_NUMBER); // Konstans használata
        when(parkingSpotRepository.findById(VALID_ID)).thenReturn(Optional.of(expected));

        Optional<ParkingSpot> result = service.getParkingSpotById(VALID_ID);
        assertTrue(result.isPresent());
        assertEquals(expected, result.get());
    }

    @Test
    void getParkingSpotByIdShouldReturnEmptyWhenNotExists() {
        when(parkingSpotRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());

        Optional<ParkingSpot> result = service.getParkingSpotById(NON_EXISTENT_ID);
        assertTrue(result.isEmpty());
    }

    @Test
    void updateParkingSpotShouldCreateArrivalEventWhenOccupancyChanges() {
        ParkingSpot existing = new ParkingSpot(SPOT_NUMBER_1);
        existing.setOccupied(false);

        ParkingSpot updated = new ParkingSpot(SPOT_NUMBER_1);
        updated.setOccupied(true);
        updated.setLicensePlate("NEW-123");

        when(parkingSpotRepository.findById(VALID_ID)).thenReturn(Optional.of(existing));
        when(parkingSpotRepository.save(any())).thenReturn(updated);

        ParkingSpot result = service.updateParkingSpot(VALID_ID, updated);
        verify(parkingEventRepository).save(argThat(event ->
                event.getEventType().equals("ARRIVAL")
        ));
        assertEquals("NEW-123", result.getLicensePlate());
    }

    @Test
    void updateParkingSpotShouldCreateDepartureEventWhenOccupancyChanges() {
        ParkingSpot existing = new ParkingSpot(SPOT_NUMBER_1);
        existing.setOccupied(true);

        ParkingSpot updated = new ParkingSpot(SPOT_NUMBER_1);
        updated.setOccupied(false);

        when(parkingSpotRepository.findById(VALID_ID)).thenReturn(Optional.of(existing));
        when(parkingSpotRepository.save(any())).thenReturn(updated);

        service.updateParkingSpot(VALID_ID, updated);
        verify(parkingEventRepository).save(argThat(event ->
                event.getEventType().equals("DEPARTURE")
        ));
    }

    @Test
    void updateParkingSpotShouldThrowWhenSpotNotFound() {
        when(parkingSpotRepository.findById(NON_EXISTENT_ID)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                service.updateParkingSpot(NON_EXISTENT_ID, new ParkingSpot())
        );
    }

    @Test
    void deleteParkingSpotShouldCreateDepartureEventForOccupiedSpot() {
        ParkingSpot occupiedSpot = new ParkingSpot(SPOT_NUMBER_1);
        occupiedSpot.setOccupied(true);
        occupiedSpot.setLicensePlate("ABC-123");
        when(parkingSpotRepository.findById(VALID_ID)).thenReturn(Optional.of(occupiedSpot));

        service.deleteParkingSpot(VALID_ID);
        verify(parkingEventRepository).save(any(ParkingEvent.class));
        verify(parkingSpotRepository).save(argThat(spot ->
                !spot.getOccupied() && spot.getLicensePlate() == null
        ));
    }

    @Test
    void deleteParkingSpotShouldNotCreateEventForFreeSpot() {
        ParkingSpot freeSpot = new ParkingSpot(SPOT_NUMBER_1);
        freeSpot.setOccupied(false);
        when(parkingSpotRepository.findById(VALID_ID)).thenReturn(Optional.of(freeSpot));

        service.deleteParkingSpot(VALID_ID);
        verify(parkingEventRepository, never()).save(any());
        verify(parkingSpotRepository).save(freeSpot);
    }

    @Test
    void addCarShouldThrowWhenSpotNotFound() {
        when(parkingSpotRepository.findBySpotNumber(NON_EXISTENT_SPOT_NUMBER)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () ->
                service.addCar(NON_EXISTENT_SPOT_NUMBER, "ABC-123", "SUV", "Black")
        );
    }
}