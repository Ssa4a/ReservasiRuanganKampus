package com.example.room_reservation.repository;

import com.example.room_reservation.model.BookingManager;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookingManagerRepository extends JpaRepository<BookingManager, Long> {
    // Metode khusus dapat ditambahkan jika diperlukan
}
