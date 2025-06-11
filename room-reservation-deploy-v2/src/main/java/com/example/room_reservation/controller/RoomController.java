package com.example.room_reservation.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.room_reservation.model.RoomStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.room_reservation.model.Room;
import com.example.room_reservation.service.BookingManagerService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RoomController {

    private static final Logger logger = LoggerFactory.getLogger(RoomController.class);

    @Autowired
    private BookingManagerService bookingManagerService;

    @GetMapping("/viewRoom")
    public String lihatRuangan() {
        return "viewRoom";
    }

    @GetMapping("/reserve/room")
    public String showReserveRoomPage() {
        return "reserveRoom";
    }

    // Endpoint untuk menampilkan ruangan berdasarkan tipe yang dipilih oleh client
    @GetMapping("/client/room/{type}")
    public String viewClientRoom(@PathVariable String type, Model model) {
        type = type.toUpperCase();  // Pastikan tipe yang diterima adalah dalam bentuk huruf besar

        // Ambil semua ruangan berdasarkan tipe dan pastikan hanya ruangan yang tersedia (AVAILABLE) yang diambil
        List<Room> rooms = bookingManagerService.getRoomsByType(type);

        // Jika tidak ada ruangan ditemukan untuk tipe tersebut
        if (rooms.isEmpty()) {
            model.addAttribute("error", "Tidak ada ruangan yang tersedia untuk tipe: " + type);
            return "error";  // Tampilkan halaman error jika tidak ada ruangan
        }

        model.addAttribute("rooms", rooms);  // Pass ruangan ke model untuk ditampilkan di halaman

        switch (type) {
            case "LAB":
                return "clientRoomLab";  // Arahkan ke tampilan ruangan Lab
            case "KELAS":
                return "clientRoomKelas";  // Arahkan ke tampilan ruangan Kelas
            case "AULA":
                return "clientRoomAula";  // Arahkan ke tampilan ruangan Aula
            default:
                model.addAttribute("error", "Tipe ruangan tidak dikenali: " + type);
                return "error";  // Tampilkan error jika tipe ruangan tidak dikenali
        }
    }

    // Endpoint to display rooms based on type for admin
    @GetMapping("/admin/rooms/{type}")
    public String showAdminRooms(@PathVariable String type, Model model) {
        try {
            type = type.toUpperCase();
            List<Room> rooms = bookingManagerService.getRoomsByType(type);
            model.addAttribute("rooms", rooms);

            switch (type) {
                case "KELAS":
                    return "adminRoomKelas";
                case "LAB":
                    return "adminRoomLab";
                case "AULA":
                    return "adminRoomAula";
                default:
                    model.addAttribute("error", "Unknown room type: " + type);
                    return "error";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching rooms for type " + type + ": " + e.getMessage());
            return "error";
        }
    }

    // Endpoint untuk memperbarui status ruangan
    @GetMapping("/admin/room/update-status/{id}")
    @ResponseBody
    public Map<String, Object> updateRoomStatus(@PathVariable Long id, @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();
        Room room = bookingManagerService.getRoomById(id);
        logger.info("Room ID: " + id + ", Status before update: " + room.getStatus());

        if (room != null) {
            if ("BOOKED".equals(status)) {
                room.setStatus(RoomStatus.BOOKED);
            } else {
                room.setStatus(RoomStatus.AVAILABLE);
            }

            bookingManagerService.saveRoom(room);
            logger.info("Room ID: " + room.getId() + " updated to status: " + room.getStatus());

            response.put("success", true);
            response.put("message", "Status ruangan berhasil diperbarui!");
        } else {
            response.put("success", false);
            response.put("message", "Ruangan tidak ditemukan!");
        }

        return response;
    }

    // Endpoint untuk menampilkan semua ruangan
    @GetMapping("/rooms")
    public String showRooms(Model model) {
        try {
            // Ambil semua ruangan
            List<Room> rooms = bookingManagerService.getAllRooms();
            model.addAttribute("rooms", rooms);

            // Mengarahkan ke view 'adminRoomKelas' untuk menampilkan data
            return "adminRoomKelas";  // nama file HTML-nya (adminRoomKelas.html)
        } catch (Exception e) {
            model.addAttribute("error", "Gagal mengambil daftar ruangan: " + e.getMessage());
            return "error";  // Halaman error jika ada masalah
        }
    }
}
