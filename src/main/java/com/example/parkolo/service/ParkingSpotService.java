package com.example.parkolo.service;

import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.repository.ParkingSpotRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository repository;

    public ParkingSpotService(ParkingSpotRepository repository) {
        this.repository = repository;
    }

    public List<ParkingSpot> getAll() {
        return repository.findAll();
    }

    public ParkingSpot save(ParkingSpot spot) {
        return repository.save(spot);
    }

    public ParkingSpot update(Long id, ParkingSpot updatedSpot) {
        return repository.findById(id)
                .map(existing -> {
                    existing.setLicensePlate(updatedSpot.getLicensePlate());
                    existing.setCarType(updatedSpot.getCarType());
                    existing.setColor(updatedSpot.getColor());
                    existing.setOccupied(updatedSpot.isOccupied());
                    return repository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Parkolóhely nem található ID alapján: " + id));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}