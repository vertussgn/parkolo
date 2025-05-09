package com.example.parkolo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/status")
    public String showStatusPage() {
        return "status"; // Ez a név megegyezik a status.html fájl nevével
    }

    @GetMapping("/index")
    public String showIndexPage() {
        return "index"; // Ez a név megegyezik az index.html fájl nevével
    }

    @GetMapping("/")
    public String redirectToIndex() {
        return "redirect:/index";
    }
}