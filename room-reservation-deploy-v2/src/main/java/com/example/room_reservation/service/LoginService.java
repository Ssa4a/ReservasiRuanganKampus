package com.example.room_reservation.service;

import com.example.room_reservation.model.UserBase;
import com.example.room_reservation.repository.UserBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    // Method untuk login menggunakan email dan password
    public UserBase login(String email, String password) {
        // Mencari pengguna berdasarkan email
        UserBase user = userBaseRepository.findByEmail(email);

        // Memeriksa apakah password cocok
        if (user != null && user.getPassword().equals(password)) {
            return user; // Login berhasil
        }

        return null; // Login gagal
    }
}
