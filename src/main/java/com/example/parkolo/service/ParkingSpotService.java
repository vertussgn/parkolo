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

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public ParkingSpot update(Long id, ParkingSpot updated) {
        return repository.findById(id).map(existing -> {
            existing.setLicensePlate(updated.getLicensePlate());
            existing.setOccupied(updated.isOccupied());
            return repository.save(existing);
        }).orElseThrow();
    }
}
