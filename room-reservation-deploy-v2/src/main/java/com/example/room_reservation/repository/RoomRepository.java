package com.example.room_reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.room_reservation.model.Room;

public interface RoomRepository extends JpaRepository<Room, Long> {
    // Menambahkan query untuk mencari ruangan berdasarkan tipe
    List<Room> findByType(String type);
}

