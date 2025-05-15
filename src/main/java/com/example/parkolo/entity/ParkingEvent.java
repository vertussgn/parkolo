package com.example.parkolo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_events")
public class ParkingEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parking_spot_id", nullable = false)
    private ParkingSpot parkingSpot;

    @Column(name = "event_type", nullable = false)
    private String eventType; // "ARRIVAL" vagy "DEPARTURE"

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "color")
    private String color;

    @Column(name = "timestamp", nullable = false)
    private LocalDateTime timestamp;

    // Konstruktorok
    public ParkingEvent() {
        this.timestamp = LocalDateTime.now();
    }

    public ParkingEvent(ParkingSpot parkingSpot, String eventType) {
        this.parkingSpot = parkingSpot;
        this.eventType = eventType;
        this.timestamp = LocalDateTime.now();

        if (parkingSpot != null) {
            this.licensePlate = parkingSpot.getLicensePlate();
            this.carType = parkingSpot.getCarType();
            this.color = parkingSpot.getColor();
        }
    }

    // Getterek és setterek
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot parkingSpot) {
        this.parkingSpot = parkingSpot;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "ParkingEvent{" +
                "id=" + id +
                ", parkingSpot=" + (parkingSpot != null ? parkingSpot.getSpotNumber() : null) +
                ", eventType='" + eventType + '\'' +
                ", licensePlate='" + licensePlate + '\'' +
                ", timestamp=" + timestamp +
                '}';
    }
}