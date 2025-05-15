package com.example.parkolo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "parking_spots")
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "spot_number", nullable = false, unique = true)
    private Integer spotNumber;

    @Column(name = "license_plate")
    private String licensePlate;

    @Column(name = "car_type")
    private String carType;

    @Column(name = "color")
    private String color;

    @Column(name = "occupied", nullable = false)
    private Boolean occupied;

    @Column(name = "last_updated")
    private LocalDateTime lastUpdated;

    // Konstruktorok
    public ParkingSpot() {
        this.occupied = false;
        this.lastUpdated = LocalDateTime.now();
    }

    public ParkingSpot(Integer spotNumber) {
        this.spotNumber = spotNumber;
        this.occupied = false;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getterek és setterek
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(Integer spotNumber) {
        this.spotNumber = spotNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
        this.lastUpdated = LocalDateTime.now();
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean occupied) {
        this.occupied = occupied;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public String toString() {
        return "ParkingSpot{" +
                "id=" + id +
                ", spotNumber=" + spotNumber +
                ", licensePlate='" + licensePlate + '\'' +
                ", carType='" + carType + '\'' +
                ", color='" + color + '\'' +
                ", occupied=" + occupied +
                ", lastUpdated=" + lastUpdated +
                '}';
    }
}