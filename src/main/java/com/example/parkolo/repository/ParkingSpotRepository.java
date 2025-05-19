package com.example.parkolo.repository;

import com.example.parkolo.entity.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@SuppressWarnings("unused")
@Repository
public interface ParkingSpotRepository
        extends JpaRepository<ParkingSpot, Long> {

    Optional<ParkingSpot> findBySpotNumber(Integer spotNumber);

    List<ParkingSpot> findByOccupied(Boolean occupied);

    @Query("SELECT p FROM ParkingSpot p ORDER BY p.spotNumber ASC")
    List<ParkingSpot> findAllOrderBySpotNumber();

    boolean existsBySpotNumber(Integer spotNumber);

    Optional<ParkingSpot> findByLicensePlate(String licensePlate);
}