package com.example.room_reservation.service;

import com.example.room_reservation.model.UserBase;
import com.example.room_reservation.repository.UserBaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserBaseRepository userBaseRepository;

    public UserBase login(String email, String password) {
        UserBase user = userBaseRepository.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            return user;
        } else {
            return null;
        }
    }
}
