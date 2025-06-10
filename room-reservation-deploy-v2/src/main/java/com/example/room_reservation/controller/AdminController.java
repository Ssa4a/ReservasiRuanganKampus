package com.example.room_reservation.controller;

import com.example.room_reservation.model.Booking;
import com.example.room_reservation.model.BookingStatus;
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
        return "redirect:/admin/dashboard";
    }

    @PostMapping("/admin/rejectBooking")
    public String rejectBooking(@RequestParam("bookingId") Long bookingId) {
        bookingService.rejectBooking(bookingId);
        return "redirect:/admin/dashboard";
    }
}
