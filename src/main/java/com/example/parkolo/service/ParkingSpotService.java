package com.example.parkolo.service;

import com.example.parkolo.entity.ParkingEvent;
import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.repository.ParkingEventRepository;
import com.example.parkolo.repository.ParkingSpotRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingEventRepository parkingEventRepository;

    @Autowired
    public ParkingSpotService(
            ParkingSpotRepository parkingSpotRepository,
            ParkingEventRepository parkingEventRepository) {
        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingEventRepository = parkingEventRepository;
    }

    // Alkalmazás indításakor inicializáljuk a parkolóhelyeket, ha még nem léteznek
    @PostConstruct
    public void initializeParkingSpots() {
        if (parkingSpotRepository.count() == 0) {
            IntStream.rangeClosed(1, 20)
                    .forEach(i -> {
                        ParkingSpot spot = new ParkingSpot();
                        spot.setSpotNumber(i);
                        spot.setOccupied(false);
                        parkingSpotRepository.save(spot);
                    });
        }
    }

    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotRepository.findAllOrderBySpotNumber();
    }

    public Optional<ParkingSpot> getParkingSpotById(Long id) {
        return parkingSpotRepository.findById(id);
    }

    public Optional<ParkingSpot> getParkingSpotByNumber(Integer spotNumber) {
        return parkingSpotRepository.findBySpotNumber(spotNumber);
    }

    @Transactional
    public ParkingSpot addCar(Integer spotNumber, String licensePlate, String carType, String color) {
        Optional<ParkingSpot> spotOptional = parkingSpotRepository.findBySpotNumber(spotNumber);

        if (spotOptional.isEmpty()) {
            throw new IllegalArgumentException("Nem létező parkolóhely: " + spotNumber);
        }

        ParkingSpot spot = spotOptional.get();

        if (spot.getOccupied()) {
            throw new IllegalStateException("A parkolóhely már foglalt: " + spotNumber);
        }

        // Autó hozzáadása
        spot.setLicensePlate(licensePlate);
        spot.setCarType(carType);
        spot.setColor(color);
        spot.setOccupied(true);

        ParkingSpot savedSpot = parkingSpotRepository.save(spot);

        // Parkolási esemény mentése
        ParkingEvent event = new ParkingEvent(savedSpot, "ARRIVAL");
        parkingEventRepository.save(event);

        return savedSpot;
    }

    @Transactional
    public ParkingSpot updateParkingSpot(Long id, ParkingSpot updatedSpot) {
        Optional<ParkingSpot> spotOptional = parkingSpotRepository.findById(id);

        if (spotOptional.isEmpty()) {
            throw new IllegalArgumentException("Nem létező parkolóhely (id): " + id);
        }

        ParkingSpot existingSpot = spotOptional.get();

        // Az állapot megváltozásának ellenőrzése
        boolean wasOccupied = existingSpot.getOccupied();
        boolean willBeOccupied = updatedSpot.getOccupied();

        // Értékek frissítése
        existingSpot.setLicensePlate(updatedSpot.getLicensePlate());
        existingSpot.setCarType(updatedSpot.getCarType());
        existingSpot.setColor(updatedSpot.getColor());
        existingSpot.setOccupied(updatedSpot.getOccupied());

        ParkingSpot savedSpot = parkingSpotRepository.save(existingSpot);

        // Parkolási esemény mentése, ha az állapot megváltozott
        if (wasOccupied && !willBeOccupied) {
            // Autó távozott
            ParkingEvent event = new ParkingEvent(savedSpot, "DEPARTURE");
            parkingEventRepository.save(event);
        } else if (!wasOccupied && willBeOccupied) {
            // Új autó érkezett
            ParkingEvent event = new ParkingEvent(savedSpot, "ARRIVAL");
            parkingEventRepository.save(event);
        }

        return savedSpot;
    }

    @Transactional
    public void deleteParkingSpot(Long id) {
        Optional<ParkingSpot> spotOptional = parkingSpotRepository.findById(id);

        if (spotOptional.isEmpty()) {
            throw new IllegalArgumentException("Nem létező parkolóhely (id): " + id);
        }

        ParkingSpot spot = spotOptional.get();

        // Ha foglalt volt, rögzítünk egy távozási eseményt
        if (spot.getOccupied()) {
            ParkingEvent event = new ParkingEvent(spot, "DEPARTURE");
            parkingEventRepository.save(event);
        }

        // Parkolóhely visszaállítása szabad állapotra
        spot.setLicensePlate(null);
        spot.setCarType(null);
        spot.setColor(null);
        spot.setOccupied(false);

        parkingSpotRepository.save(spot);
    }

    public List<ParkingSpot> getOccupiedParkingSpots() {
        return parkingSpotRepository.findByOccupied(true);
    }

    public List<ParkingSpot> getFreeParkingSpots() {
        return parkingSpotRepository.findByOccupied(false);
    }
}