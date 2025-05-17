package com.example.parkolo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Column;
import jakarta.persistence.GenerationType;
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

    public ParkingSpot(Integer aSpotNumber) { // Paraméter átnevezve
        this.spotNumber = aSpotNumber;
        this.occupied = false;
        this.lastUpdated = LocalDateTime.now();
    }

    // Getterek és setterek
    public Long getId() {
        return id;
    }

    public void setId(Long aId) { // Paraméter átnevezve
        this.id = aId;
    }

    public Integer getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(Integer aSpotNumber) { // Paraméter átnevezve
        this.spotNumber = aSpotNumber;
    }

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String aLicensePlate) { // Paraméter átnevezve
        this.licensePlate = aLicensePlate;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String aCarType) { // Paraméter átnevezve
        this.carType = aCarType;
        this.lastUpdated = LocalDateTime.now();
    }

    public String getColor() {
        return color;
    }

    public void setColor(String aColor) { // Paraméter átnevezve
        this.color = aColor;
        this.lastUpdated = LocalDateTime.now();
    }

    public Boolean getOccupied() {
        return occupied;
    }

    public void setOccupied(Boolean aOccupied) { // Paraméter átnevezve
        this.occupied = aOccupied;
        this.lastUpdated = LocalDateTime.now();
    }

    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(LocalDateTime aLastUpdated) {
        this.lastUpdated = aLastUpdated;
    }

    @Override
    public String toString() {
        return "ParkingSpot{"
                + "id=" + id
                + ", spotNumber=" + spotNumber
                + ", licensePlate='" + licensePlate
                + '\'' + ", carType='" + carType + '\''
                + ", color='" + color + '\''
                + ", occupied=" + occupied
                + ", lastUpdated=" + lastUpdated
                + '}';
    }
}