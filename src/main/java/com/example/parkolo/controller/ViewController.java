package com.example.parkolo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String showHomePage() {
        return "index"; // templates/index.html
    }

    @GetMapping("/add-car")
    public String showAddCarPage() {
        return "add-car"; // templates/add-car.html
    }

    @GetMapping("/status")
    public String showStatusPage() {
        return "status"; // templates/status.html
    }
}