package com.example.room_reservation.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.room_reservation.model.Booking;
import com.example.room_reservation.model.UserBase;
import com.example.room_reservation.service.BookingManagerService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/client")
public class ClientDashboardController {

    private static final Logger logger = LoggerFactory.getLogger(ClientDashboardController.class);

    @Autowired
    private BookingManagerService bookingManagerService;

    @GetMapping("/dashboard")
    public String showClientDashboard() {
        return "clientdashboard";
    }

    @GetMapping("/booking/history")
    public String viewBookingHistory(Model model, HttpSession session) {
        logger.info("Accessing booking history...");

        UserBase user = (UserBase) session.getAttribute("user");

        if (user == null) {
            logger.warn("No user found in session, redirecting to login");
            return "redirect:/login";
        }

        logger.info("User found in session: {}", user.getUsername());

        try {
            List<Booking> bookings = bookingManagerService.getBookingsForUser(user);
            logger.info("Found {} bookings for user", bookings.size());

            for (Booking booking : bookings) {
                logger.info("Booking ID: {}, Room: {}, Date: {}, Status: {}",
                        booking.getId(),
                        booking.getRoom() != null ? booking.getRoom().getName() : "NULL",
                        booking.getBookingDate(),
                        booking.getStatus());
            }

            model.addAttribute("bookings", bookings);
            model.addAttribute("user", user);

        } catch (Exception e) {
            logger.error("Error fetching booking history", e);
            model.addAttribute("error", "Terjadi kesalahan saat mengambil riwayat booking");
        }

        return "bookingHistory";
    }

    @GetMapping("/request-booking")
    public String requestBooking(Model model) {
        model.addAttribute("rooms", bookingManagerService.getAllRooms());
        return "requestBooking";
    }
}
