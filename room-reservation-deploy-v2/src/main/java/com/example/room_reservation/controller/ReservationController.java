package com.example.room_reservation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ReservationController {

    @GetMapping("/submit-reservation")
    public String showReservationForm() {
        return "submit-reservation"; // Menampilkan form
    }

    public String handleReservation(
        @RequestParam("date") String date,
        @RequestParam("purpose") String purpose,
        @RequestParam("userType") String userType,
        Model model
    ) {
        // Proses data (misalnya: simpan ke database, validasi, dll.)
        // Untuk demo, kita kirim balik ke halaman konfirmasi sederhana

        model.addAttribute("date", date);
        model.addAttribute("purpose", purpose);
        model.addAttribute("userType", userType);

        return "reservation-success"; // Tampilkan halaman sukses
    }
}
