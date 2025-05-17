package com.example.parkolo.repository;

import com.example.parkolo.entity.ParkingEvent;
import com.example.parkolo.entity.ParkingSpot;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ParkingEventRepository
        extends JpaRepository<ParkingEvent, Long> {

    List<ParkingEvent> findByParkingSpot(ParkingSpot parkingSpot);

    List<ParkingEvent> findByParkingSpotAndEventType(
            ParkingSpot parkingSpot,
            String eventType
    );

    List<ParkingEvent> findByTimestampBetween(
            LocalDateTime start,
            LocalDateTime end
    );

    @Query("SELECT e FROM ParkingEvent e "
            + "WHERE e.parkingSpot.spotNumber = :spotNumber "
            + "ORDER BY e.timestamp DESC")
    List<ParkingEvent> findEventsBySpotNumber(Integer spotNumber);

    List<ParkingEvent> findByLicensePlate(String licensePlate);
}