package com.example.room_reservation.controller;

import com.example.room_reservation.model.*;
import com.example.room_reservation.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    @PostMapping("/submit-reservation")
    public String submitReservation(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("purpose") String purpose,
            @RequestParam("userType") String userType,
            @RequestParam("roomId") Long roomId,
            Model model
    ) {
        // Dummy user (gunakan user login setelah sistem autentikasi aktif)
        UserBase user = userBaseRepository.findById(1L).orElse(null);
        Room room = roomRepository.findById(roomId).orElse(null);

        if (user == null || room == null) {
            model.addAttribute("error", "User atau Room tidak ditemukan.");
            return "error";
        }

        // Buat booking baru
        Booking booking = new Booking(user, room, date, purpose, userType);
        booking.setStatus(BookingStatus.PENDING);
        bookingRepository.save(booking);

        model.addAttribute("message", "Reservasi berhasil diajukan.");
        return "reservation-success"; // pastikan ada file reservation-success.html di templates
    }
}