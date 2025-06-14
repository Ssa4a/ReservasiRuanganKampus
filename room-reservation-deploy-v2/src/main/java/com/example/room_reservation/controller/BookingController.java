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
import java.util.stream.Collectors;

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
        // Ambil hanya ruangan yang AVAILABLE untuk ditampilkan di dropdown
        List<Room> allRooms = roomRepository.findAll();
        List<Room> rooms = allRooms.stream()
                .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                .collect(Collectors.toList());
        model.addAttribute("rooms", rooms);
        return "submit-reservation";
    }

    // GET method untuk menampilkan form reservasi (route baru)
    @GetMapping("/reserve-room")
    public String showReserveRoomForm(Model model) {
        try {
            // Ambil semua ruangan dari database
            List<Room> allRooms = roomRepository.findAll();

            // Debug log untuk melihat SEMUA ruangan di database
            System.out.println("=== LOADING ALL ROOMS FROM DATABASE ===");
            System.out.println("Total rooms found in database: " + allRooms.size());

            if (allRooms.isEmpty()) {
                System.out.println("WARNING: No rooms found in database at all!");
            } else {
                System.out.println("=== ALL ROOMS IN DATABASE ===");
                for (Room room : allRooms) {
                    System.out.println("Room ID: " + room.getId() +
                            " - Name: " + room.getName() +
                            " - Type: " + room.getType() +
                            " - Status: " + room.getStatus());
                }
                System.out.println("=== END ALL ROOMS ===");
            }

            // Filter hanya ruangan yang AVAILABLE
            List<Room> availableRooms = allRooms.stream()
                    .filter(room -> room.getStatus() == RoomStatus.AVAILABLE)
                    .collect(Collectors.toList());

            // Debug log untuk melihat ruangan AVAILABLE
            System.out.println("=== FILTERING AVAILABLE ROOMS ===");
            System.out.println("Available rooms found: " + availableRooms.size());

            if (availableRooms.isEmpty()) {
                System.out.println("WARNING: No available rooms found after filtering!");
                model.addAttribute("noRoomsMessage", "Saat ini tidak ada ruangan yang tersedia untuk reservasi.");
            } else {
                System.out.println("=== AVAILABLE ROOMS AFTER FILTERING ===");
                for (Room room : availableRooms) {
                    System.out.println("Available Room ID: " + room.getId() +
                            " - Name: " + room.getName() +
                            " - Type: " + room.getType() +
                            " - Status: " + room.getStatus());
                }
                System.out.println("=== END AVAILABLE ROOMS ===");
            }

            // Debug: Cek berdasarkan tipe ruangan
            System.out.println("=== BREAKDOWN BY ROOM TYPE ===");
            long kelasCount = allRooms.stream().filter(r -> "KELAS".equals(r.getType())).count();
            long aulaCount = allRooms.stream().filter(r -> "AULA".equals(r.getType())).count();
            long labCount = allRooms.stream().filter(r -> "LAB".equals(r.getType())).count();

            long kelasAvailable = allRooms.stream().filter(r -> "KELAS".equals(r.getType()) && r.getStatus() == RoomStatus.AVAILABLE).count();
            long aulaAvailable = allRooms.stream().filter(r -> "AULA".equals(r.getType()) && r.getStatus() == RoomStatus.AVAILABLE).count();
            long labAvailable = allRooms.stream().filter(r -> "LAB".equals(r.getType()) && r.getStatus() == RoomStatus.AVAILABLE).count();

            System.out.println("KELAS - Total: " + kelasCount + ", Available: " + kelasAvailable);
            System.out.println("AULA - Total: " + aulaCount + ", Available: " + aulaAvailable);
            System.out.println("LAB - Total: " + labCount + ", Available: " + labAvailable);
            System.out.println("=== END BREAKDOWN ===");

            model.addAttribute("rooms", availableRooms);
            System.out.println("Available rooms attribute added to model successfully");
            System.out.println("=== END LOADING AVAILABLE ROOMS ===");

        } catch (Exception e) {
            System.out.println("ERROR loading available rooms: " + e.getMessage());
            e.printStackTrace();
            model.addAttribute("error", "Terjadi kesalahan saat memuat data ruangan: " + e.getMessage());
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
                result.append("<h3>All Rooms:</h3>");
                for (Room room : rooms) {
                    result.append("ID: ").append(room.getId())
                            .append(" - Name: ").append(room.getName())
                            .append(" - Type: ").append(room.getType())
                            .append(" - Status: ").append(room.getStatus())
                            .append("<br>");
                }

                // Breakdown berdasarkan tipe
                result.append("<br><h3>Breakdown by Type:</h3>");
                long kelasTotal = rooms.stream().filter(r -> "KELAS".equals(r.getType())).count();
                long aulaTotal = rooms.stream().filter(r -> "AULA".equals(r.getType())).count();
                long labTotal = rooms.stream().filter(r -> "LAB".equals(r.getType())).count();

                long kelasAvailable = rooms.stream().filter(r -> "KELAS".equals(r.getType()) && r.getStatus() == RoomStatus.AVAILABLE).count();
                long aulaAvailable = rooms.stream().filter(r -> "AULA".equals(r.getType()) && r.getStatus() == RoomStatus.AVAILABLE).count();
                long labAvailable = rooms.stream().filter(r -> "LAB".equals(r.getType()) && r.getStatus() == RoomStatus.AVAILABLE).count();

                result.append("<b>KELAS:</b> Total = ").append(kelasTotal).append(", Available = ").append(kelasAvailable).append("<br>");
                result.append("<b>AULA:</b> Total = ").append(aulaTotal).append(", Available = ").append(aulaAvailable).append("<br>");
                result.append("<b>LAB:</b> Total = ").append(labTotal).append(", Available = ").append(labAvailable).append("<br>");

                // Tampilkan ruangan yang tidak available
                result.append("<br><h3>Non-Available Rooms:</h3>");
                List<Room> nonAvailable = rooms.stream()
                        .filter(r -> r.getStatus() != RoomStatus.AVAILABLE)
                        .collect(Collectors.toList());

                if (nonAvailable.isEmpty()) {
                    result.append("All rooms are AVAILABLE<br>");
                } else {
                    for (Room room : nonAvailable) {
                        result.append("ID: ").append(room.getId())
                                .append(" - Name: ").append(room.getName())
                                .append(" - Type: ").append(room.getType())
                                .append(" - Status: ").append(room.getStatus())
                                .append("<br>");
                    }
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

            // CEK STATUS RUANGAN - JIKA BOOKED, KEMBALIKAN ERROR (DOUBLE CHECK)
            if (room.getStatus() == RoomStatus.BOOKED) {
                System.out.println("ERROR: Room " + room.getName() + " sudah di-booking");
                model.addAttribute("error", "Ruangan " + room.getName() + " sudah tidak tersedia.");
                // Kembalikan ke form dengan data rooms AVAILABLE saja
                List<Room> allRooms = roomRepository.findAll();
                List<Room> rooms = allRooms.stream()
                        .filter(r -> r.getStatus() == RoomStatus.AVAILABLE)
                        .collect(Collectors.toList());
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