package com.example.room_reservation.service;

import com.example.room_reservation.model.*;
import com.example.room_reservation.repository.BookingManagerRepository;
import com.example.room_reservation.repository.BookingRepository;
import com.example.room_reservation.repository.RoomRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BookingManagerService {

    private static final Logger logger = LoggerFactory.getLogger(BookingManagerService.class);


    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private BookingManagerRepository bookingManagerRepository;  // Jika ada repository untuk BookingManager
    @Autowired
    private RoomRepository roomRepository;

    // Method untuk mendapatkan semua ruangan yang dikelola oleh BookingManager
    public List<Room> getAllRooms() {
        BookingManager bookingManager = bookingManagerRepository.findById(1L).orElseThrow(() -> new RuntimeException("BookingManager not found"));
        return bookingManager.getRooms();  // Mengambil daftar ruangan dari BookingManager
    }

    // Method untuk mendapatkan booking berdasarkan user
    public List<Booking> getBookingsForUser(UserBase user) {
        return bookingRepository.findByUser(user);  // Misalnya, Anda dapat menambahkan metode pencarian berdasarkan user di BookingRepository
    }

    // Method untuk membatalkan booking
    public boolean cancelBooking(int bookingId) {
        Booking booking = bookingRepository.findById((long) bookingId).orElse(null);
        if (booking != null) {
            bookingRepository.delete(booking);
            return true;
        }
        return false;
    }

    // Method untuk mendapatkan bookings berdasarkan status
    public List<Booking> getBookingsByStatusAndUser(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }

    // Metode untuk mendapatkan ruangan berdasarkan ID
    public Room getRoomById(Long id) {
        return roomRepository.findById(id).orElse(null);
    }

    // Metode untuk menyimpan perubahan status ruangan
    @Transactional  // Pastikan transaksi berjalan
    public void saveRoom(Room room) {
        roomRepository.save(room);  // Menyimpan perubahan status ruangan ke database
    }

    // Method untuk mendapatkan ruangan berdasarkan tipe
    public List<Room> getRoomsByType(String type) {
        return roomRepository.findByType(type.toUpperCase());  // Menggunakan RoomRepository untuk mencari ruangan berdasarkan tipe
}
}
