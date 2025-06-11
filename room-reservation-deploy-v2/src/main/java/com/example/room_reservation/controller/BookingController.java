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

    // GET method untuk menampilkan form reservasi (route lama)
    @GetMapping("/submit-reservation")
    public String showReservationForm(Model model) {
        // Ambil semua ruangan dari database untuk ditampilkan di dropdown
        List<Room> rooms = roomRepository.findAll();
        model.addAttribute("rooms", rooms);
        return "submit-reservation";
    }

    // GET method untuk menampilkan form reservasi (route baru)
    @GetMapping("/reserve-room")
    public String showReserveRoomForm(Model model) {
        try {
            // Ambil semua ruangan dari database
            List<Room> rooms = roomRepository.findAll();

            // Debug log untuk melihat data rooms
            System.out.println("=== LOADING ROOMS FOR /reserve-room ===");
            System.out.println("Total rooms found: " + rooms.size());

            if (rooms.isEmpty()) {
                System.out.println("WARNING: No rooms found in database!");
            } else {
                for (Room room : rooms) {
                    System.out.println("Room ID: " + room.getId() +
                            " - Name: " + room.getName() +
                            " - Type: " + room.getType() +
                            " - Status: " + room.getStatus());
                }
            }

            model.addAttribute("rooms", rooms);
            System.out.println("Rooms attribute added to model successfully");
            System.out.println("=== END LOADING ROOMS ===");

        } catch (Exception e) {
            System.out.println("ERROR loading rooms: " + e.getMessage());
            e.printStackTrace();
        }

        return "reserveRoom"; // nama file HTML tanpa .html
    }

    // Method untuk test debugging - HAPUS SETELAH TESTING
    @GetMapping("/test-rooms")
    @ResponseBody
    public String testRooms() {
        try {
            List<Room> rooms = roomRepository.findAll();
            StringBuilder result = new StringBuilder();
            result.append("<h2>Room Database Test</h2>");
            result.append("Total rooms: ").append(rooms.size()).append("<br><br>");

            if (rooms.isEmpty()) {
                result.append("<b>No rooms found in database!</b>");
            } else {
                for (Room room : rooms) {
                    result.append("ID: ").append(room.getId())
                            .append(" - Name: ").append(room.getName())
                            .append(" - Type: ").append(room.getType())
                            .append(" - Status: ").append(room.getStatus())
                            .append("<br>");
                }
            }
            return result.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
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
            // Debug logs
            System.out.println("=== BOOKING SUBMISSION ===");
            System.out.println("Date: " + date);
            System.out.println("Purpose: " + purpose);
            System.out.println("User Type: " + userType);
            System.out.println("Room ID: " + roomId);

            // Ambil user (gunakan user login setelah sistem autentikasi aktif)
            UserBase user = userBaseRepository.findById(1L).orElse(null);

            // Ambil room berdasarkan roomId yang dipilih
            Room room = roomRepository.findById(roomId).orElse(null);

            if (user == null) {
                System.out.println("ERROR: User tidak ditemukan");
                model.addAttribute("error", "User tidak ditemukan.");
                // Kembalikan ke form dengan data rooms
                List<Room> rooms = roomRepository.findAll();
                model.addAttribute("rooms", rooms);
                return "reserveRoom";
            }

            if (room == null) {
                System.out.println("ERROR: Room dengan ID " + roomId + " tidak ditemukan");
                model.addAttribute("error", "Ruangan tidak ditemukan.");
                // Kembalikan ke form dengan data rooms
                List<Room> rooms = roomRepository.findAll();
                model.addAttribute("rooms", rooms);
                return "reserveRoom";
            }

            System.out.println("User found: " + user.getUsername());
            System.out.println("Room found: " + room.getName() + " (Type: " + room.getType() + ")");

            // Buat booking baru
            Booking booking = new Booking(user, room, date, purpose, userType);
            booking.setStatus(BookingStatus.PENDING);

            // Simpan ke database
            Booking savedBooking = bookingRepository.save(booking);

            // Log untuk debugging
            System.out.println("SUCCESS: Booking saved with ID: " + savedBooking.getId());
            System.out.println("Room: " + savedBooking.getRoom().getName());
            System.out.println("Status: " + savedBooking.getStatus());
            System.out.println("=== END BOOKING SUBMISSION ===");

            model.addAttribute("message", "Reservasi berhasil diajukan untuk ruangan: " + room.getName());
            model.addAttribute("booking", savedBooking);
            return "reservation-success";

        } catch (Exception e) {
            System.out.println("ERROR: Exception occurred: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan: " + e.getMessage());
            // Kembalikan ke form dengan data rooms
            List<Room> rooms = roomRepository.findAll();
            model.addAttribute("rooms", rooms);
            return "reserveRoom";
        }
    }
}