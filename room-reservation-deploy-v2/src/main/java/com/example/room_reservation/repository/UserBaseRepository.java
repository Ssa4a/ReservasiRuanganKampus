package com.example.room_reservation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;  // Pastikan enum Role sudah di-import
import org.springframework.stereotype.Repository;

import com.example.room_reservation.model.Role;
import com.example.room_reservation.model.UserBase;

@Repository
public interface UserBaseRepository extends JpaRepository<UserBase, Long>
 {

    // Mencari User berdasarkan email
    UserBase findByEmail(String email);

    // Pencarian berdasarkan role
    List<UserBase> findByRole(Role role);  // Menggunakan enum Role
}
