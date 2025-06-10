package com.example.room_reservation.repository;

import com.example.room_reservation.model.Booking;
import com.example.room_reservation.model.BookingStatus;
import com.example.room_reservation.model.UserBase;
import com.example.room_reservation.model.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    // Pencarian berdasarkan status
    List<Booking> findByStatus(BookingStatus status);

    // Pencarian berdasarkan user
    List<Booking> findByUser(UserBase user);

    // Pencarian berdasarkan roomId dan status (menggunakan room.id)
    List<Booking> findByRoom_IdAndStatus(Long roomId, BookingStatus status);  // Menggunakan room.id untuk pencarian
}

