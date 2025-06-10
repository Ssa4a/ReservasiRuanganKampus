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
import java.util.List;

@Controller
public class BookingController {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @Autowired
    private UserBaseRepository userBaseRepository;

    // GET method untuk menampilkan form reservasi
    @GetMapping("/submit-reservation")
    public String showReservationForm(Model model) {
        // Ambil semua ruangan dari database untuk ditampilkan di dropdown
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "submit-reservation";
    }

    @PostMapping("/submit-reservation")
    public String submitReservation(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("purpose") String purpose,
            @RequestParam("userType") String userType,
            @RequestParam("roomId") Long roomId,
            Model model
    ) {
        try {
            // Ambil user (gunakan user login setelah sistem autentikasi aktif)
            UserBase user = userBaseRepository.findById(1L).orElse(null);

            // Ambil room berdasarkan roomId yang dipilih
            Room room = roomRepository.findById(roomId).orElse(null);

            if (user == null) {
                model.addAttribute("error", "User tidak ditemukan.");
                return "error";
            }

            if (room == null) {
                model.addAttribute("error", "Ruangan tidak ditemukan.");
                return "error";
            }

            // Buat booking baru
            Booking booking = new Booking(user, room, date, purpose, userType);
            booking.setStatus(BookingStatus.PENDING);

            // Simpan ke database
            Booking savedBooking = bookingRepository.save(booking);

            // Log untuk debugging
            System.out.println("Booking saved with ID: " + savedBooking.getId());
            System.out.println("Room: " + savedBooking.getRoom().getName());

            System.out.println("Status: " + savedBooking.getStatus());

            model.addAttribute("message", "Reservasi berhasil diajukan untuk ruangan: " + room.getName());
            model.addAttribute("booking", savedBooking);
            return "reservation-success";

        } catch (Exception e) {
            model.addAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            return "error";
        }
    }
}