package com.example.room_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")  // Mapping untuk URL homepage
    public String homePage() {
        return "home";  // Mengarahkan ke home.html
    }
}