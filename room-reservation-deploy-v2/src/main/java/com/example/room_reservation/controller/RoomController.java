package com.example.room_reservation.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.room_reservation.model.RoomStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;  // Import service yang benar
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
    private BookingManagerService bookingManagerService;  // Ubah menjadi service, bukan entitas

    @GetMapping("/viewRoom")
    public String lihatRuangan() {
        return "viewRoom";  // Mengarah ke file viewRoom.html di folder templates
    }

    @GetMapping("/reserve/room")
    public String showReserveRoomPage() {
        return "reserveRoom";
    }

    // Endpoint untuk menampilkan ruangan berdasarkan tipe yang dipilih oleh client
    @GetMapping("/client/room/{type}")
    public String viewClientRoom(@PathVariable String type, Model model) {
        // Menyesuaikan tipe ruangan dengan huruf besar
        type = type.toUpperCase();  // Mengubah tipe ruangan yang diminta menjadi huruf besar

        // Mengambil ruangan berdasarkan tipe
        List<Room> rooms = bookingManagerService.getRoomsByType(type);
        model.addAttribute("rooms", rooms);  // Menambahkan daftar ruangan ke model

        // Mengarahkan ke tampilan yang sesuai berdasarkan tipe ruangan
        switch (type) {
            case "LAB":
                return "clientRoomLab";  // Menampilkan clientRoomLab.html
            case "KELAS":
                return "clientRoomKelas";  // Menampilkan clientRoomKelas.html
            case "AULA":
                return "clientRoomAula";  // Menampilkan clientRoomAula.html
            default:
                return "clientdashboard";  // Halaman default jika tipe tidak dikenali
        }
    }

    // Endpoint to display rooms based on type for admin
    @GetMapping("/admin/rooms/{type}")
    public String showAdminRooms(@PathVariable String type, Model model) {
        try {
            // Convert the type to upper case to match the expected format
            type = type.toUpperCase();

            // Retrieve rooms based on the type
            List<Room> rooms = bookingManagerService.getRoomsByType(type);
            model.addAttribute("rooms", rooms);  // Add rooms to the model

            // Use switch case to handle different room types for admin
            switch (type) {
                case "KELAS":
                    return "adminRoomKelas";  // Template for classroom rooms
                case "LAB":
                    return "adminRoomLab";  // Template for lab rooms
                case "AULA":
                    return "adminRoomAula";  // Template for aula rooms
                default:
                    model.addAttribute("error", "Unknown room type: " + type);
                    return "error";  // If the type is not recognized, show an error page
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error fetching rooms for type " + type + ": " + e.getMessage());
            return "error";  // Display error page if something goes wrong
        }
    }

    // Endpoint untuk memperbarui status ruangan
    // Endpoint untuk memperbarui status ruangan
    @GetMapping("/admin/room/update-status/{id}")
    @ResponseBody
    public Map<String, Object> updateRoomStatus(@PathVariable Long id, @RequestParam String status) {
        Map<String, Object> response = new HashMap<>();

        // Ambil ruangan berdasarkan ID
        Room room = bookingManagerService.getRoomById(id);
        logger.info("Room ID: " + id + ", Status before update: " + room.getStatus());

        if (room != null) {
            // Mengubah status ruangan
            if ("BOOKED".equals(status)) {
                room.setStatus(RoomStatus.BOOKED);  // Set status ke BOOKED
            } else {
                room.setStatus(RoomStatus.AVAILABLE);  // Set status ke AVAILABLE
            }

            // Simpan perubahan status ke database
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
}
