package com.example.parkolo.controller;

import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.service.ParkingSpotService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/spots")
@CrossOrigin
public class ParkingSpotController {

    private final ParkingSpotService service;

    public ParkingSpotController(ParkingSpotService service) {
        this.service = service;
    }

    @GetMapping
    public List<ParkingSpot> getAll() {
        return service.getAll();
    }

    @PostMapping
    public ParkingSpot create(@RequestBody ParkingSpot spot) {
        return service.save(spot);
    }

    @PutMapping("/{id}")
    public ParkingSpot update(@PathVariable Long id, @RequestBody ParkingSpot spot) {
        return service.update(id, spot);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}