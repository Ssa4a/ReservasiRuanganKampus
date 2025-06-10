package com.example.room_reservation.controller;

import java.util.List;

import com.example.room_reservation.model.Room;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.room_reservation.model.Booking;
import com.example.room_reservation.model.UserBase;
import com.example.room_reservation.service.BookingManagerService;  // Import service yang benar

@Controller
@RequestMapping("/client")
public class ClientDashboardController {
    
    @GetMapping("/dashboard")
    public String showClientDashboard() {
        return "clientdashboard";
    }

    @Autowired
    private BookingManagerService bookingManagerService;  // Gantilah menjadi service

    // Fungsi untuk melihat riwayat booking
    @GetMapping("/booking-history")
    public String viewBookingHistory(Model model, UserBase user) {
        // Mendapatkan riwayat booking untuk user yang login
        List<Booking> bookings = bookingManagerService.getBookingsForUser(user);  // Menggunakan service yang benar
        model.addAttribute("bookings", bookings);
        return "bookingHistory";  // Menampilkan riwayat booking
    }

    @GetMapping("/request-booking")
    public String requestBooking(Model model) {
        // Menampilkan form untuk membuat pemesanan baru
        model.addAttribute("rooms", bookingManagerService.getAllRooms());  // Mengambil semua ruangan yang tersedia
        return "requestBooking";  // Halaman untuk melakukan pemesanan ruangan
    }
}
