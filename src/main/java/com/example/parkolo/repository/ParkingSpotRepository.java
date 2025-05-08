package com.example.parkolo.repository;

import com.example.parkolo.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {
    List<ParkingSpot> findAllByOrderBySpotNumberAsc();
    Optional<ParkingSpot> findBySpotNumber(Integer spotNumber);
}
