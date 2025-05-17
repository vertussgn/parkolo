package com.example.parkolo.controller;

import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/spots")
public class ParkingSpotController {

    private static final int MAX_SPOT_NUMBER = 20;
    private final ParkingSpotService parkingSpotService;

    @Autowired
    public ParkingSpotController(ParkingSpotService parkingSpotServiceParam) {
        this.parkingSpotService = parkingSpotServiceParam;
    }

    @GetMapping
    public List<ParkingSpot> getAllParkingSpots() {
        return parkingSpotService.getAllParkingSpots();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpot> getParkingSpotById(
            @PathVariable Long id) {
        Optional<ParkingSpot> spot = parkingSpotService.getParkingSpotById(id);
        return spot.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<ParkingSpot> addCar(
            @RequestBody Map<String, Object> request) {
        try {
            Integer spotNumber = (Integer) request.get("spotNumber");
            String licensePlate = (String) request.get("licensePlate");
            String carType = (String) request.get("carType");
            String color = (String) request.get("color");

            // Validation
            if (spotNumber == null || spotNumber < 1
                    || spotNumber > MAX_SPOT_NUMBER) {
                return ResponseEntity.badRequest().build();
            }

            if (licensePlate == null || licensePlate.trim().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }

            ParkingSpot spot = parkingSpotService.addCar(
                    spotNumber, licensePlate, carType, color);
            return ResponseEntity.ok(spot);
        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpot> updateParkingSpot(
            @PathVariable Long id,
            @RequestBody ParkingSpot updatedSpot) {
        try {
            ParkingSpot spot
                    = parkingSpotService.updateParkingSpot(id, updatedSpot);
            return ResponseEntity.ok(spot);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteParkingSpot(@PathVariable Long id) {
        try {
            parkingSpotService.deleteParkingSpot(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}