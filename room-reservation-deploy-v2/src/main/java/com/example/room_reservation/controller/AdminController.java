package com.example.room_reservation.controller;

import com.example.room_reservation.model.Booking;
import com.example.room_reservation.model.BookingStatus;
import com.example.room_reservation.model.Room;
import com.example.room_reservation.model.RoomStatus;
import com.example.room_reservation.repository.BookingRepository;
import com.example.room_reservation.repository.RoomRepository;
import com.example.room_reservation.service.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;

@Controller
public class AdminController {

    @Autowired
    private BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RoomRepository roomRepository;

    @GetMapping("/admin/dashboard")
    public String showAdminDashboard(Model model) {
        List<Booking> pendingBookings = bookingService.getBookingsByStatus(BookingStatus.PENDING);
        if (pendingBookings == null) {
            pendingBookings = Collections.emptyList();
        }
        model.addAttribute("pendingBookings", pendingBookings);
        return "adminDashboard";
    }

    @PostMapping("/admin/approveBooking")
    public String approveBooking(@RequestParam("bookingId") Long bookingId) {
        bookingService.acceptBooking(bookingId);

        // Kode ini sekarang harusnya work
        Booking booking = bookingRepository.findById(bookingId).orElse(null);
        if (booking != null) {
            Room room = booking.getRoom();
            room.setStatus(RoomStatus.valueOf("BOOKED"));
            roomRepository.save(room);
        }

        return "redirect:/admin/dashboard";
    }
}
