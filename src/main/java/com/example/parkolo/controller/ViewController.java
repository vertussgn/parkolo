package com.example.parkolo.controller;

import com.example.parkolo.entity.ParkingSpot;
import com.example.parkolo.service.ParkingSpotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ViewController {

    private final ParkingSpotService parkingSpotService;

    @Autowired
    public ViewController(ParkingSpotService
                                      parkingSpotServiceParam) {
        this.parkingSpotService = parkingSpotServiceParam;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping("/status")
    public String status(Model model) {
        List<ParkingSpot> parkingSpots
                = parkingSpotService.getAllParkingSpots();
        model.addAttribute("parkingSpots", parkingSpots);
        return "status";
    }

    @GetMapping("/addcar")
    public String addCar(Model model) {
        List<ParkingSpot> parkingSpots
                = parkingSpotService.getAllParkingSpots();

        long freeCount = parkingSpots.stream().filter(spot
                -> !spot.getOccupied()).count();
        long occupiedCount
                = parkingSpots.stream().filter(
                        ParkingSpot::getOccupied).count();
        long totalCount = parkingSpots.size();

        model.addAttribute("parkingSpots", parkingSpots);
        model.addAttribute("freeCount", freeCount);
        model.addAttribute("occupiedCount", occupiedCount);
        model.addAttribute("totalCount", totalCount);

        return "add-car";
    }
}