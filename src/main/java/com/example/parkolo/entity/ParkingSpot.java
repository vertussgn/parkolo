package com.example.parkolo.entity;

import jakarta.persistence.*;

@Entity
public class ParkingSpot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private int spotNumber;

    private String licensePlate;
    private String carType;
    private String color;
    private boolean occupied;

    public ParkingSpot() {}

    public ParkingSpot(int spotNumber, String licensePlate, String carType, String color, boolean occupied) {
        this.spotNumber = spotNumber;
        this.licensePlate = licensePlate;
        this.carType = carType;
        this.color = color;
        this.occupied = occupied;
    }

    public Long getId() {
        return id;
    }

    public int getSpotNumber() {
        return spotNumber;
    }

    public void setSpotNumber(int spotNumber) {
        this.spotNumber = spotNumber;
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

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
    }
}

