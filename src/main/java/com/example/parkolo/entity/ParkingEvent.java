package com.example.parkolo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Column;
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
    private String eventType;

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

    public ParkingEvent(ParkingSpot aParkingSpot, String aEventType) {
        this.parkingSpot = aParkingSpot;
        this.eventType = aEventType;
        this.timestamp = LocalDateTime.now();

        if (aParkingSpot != null) {
            this.licensePlate = aParkingSpot.getLicensePlate();
            this.carType = aParkingSpot.getCarType();
            this.color = aParkingSpot.getColor();
        }
    }

    // Getterek és setterek
    public Long getId() {
        return id;
    }

    public void setId(Long aId) {
        this.id = aId;
    }

    public ParkingSpot getParkingSpot() {
        return parkingSpot;
    }

    public void setParkingSpot(ParkingSpot aParkingSpot) {
        this.parkingSpot = aParkingSpot;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String aEventType) {
        this.eventType = aEventType;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String aLicensePlate) {
        this.licensePlate = aLicensePlate;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String aCarType) {
        this.carType = aCarType;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String aColor) {
        this.color = aColor;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime aTimestamp) {
        this.timestamp = aTimestamp;
    }

    @Override
    public String toString() {
        return "ParkingEvent{"
                + "id="
                + id
                + ", parkingSpot="
                + (parkingSpot != null ? parkingSpot.getSpotNumber() : null)
                + ", eventType='"
                + eventType + '\''
                + ", licensePlate='"
                + licensePlate + '\''
                + ", timestamp="
                + timestamp + '}';
    }
}