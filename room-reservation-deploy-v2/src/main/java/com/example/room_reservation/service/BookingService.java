package com.example.room_reservation.service;

import com.example.room_reservation.model.Booking;
import com.example.room_reservation.model.BookingStatus;
import com.example.room_reservation.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    // Terima booking (tanpa BookingManager dulu)
    public void acceptBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.APPROVED);
        bookingRepository.save(booking);
    }

    // Tolak booking (tanpa BookingManager dulu)
    public void rejectBooking(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId)
            .orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(BookingStatus.REJECTED);
        bookingRepository.save(booking);
    }

    // Ambil list booking berdasarkan status
    public List<Booking> getBookingsByStatus(BookingStatus status) {
        return bookingRepository.findByStatus(status);
    }
}
