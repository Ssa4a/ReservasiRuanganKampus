package com.example.room_reservation.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("CLIENT")  // Mengganti "MAHASISWA" dengan "CLIENT"
public class MahasiswaDosen extends UserBase {

    // Daftar untuk menyimpan riwayat booking
    @OneToMany(mappedBy = "user")
    private List<Booking> bookingHistory = new ArrayList<>();  // Inisialisasi dengan ArrayList

    // Konstruktor default tanpa parameter
    public MahasiswaDosen() {
        super("default_username", "default_email@example.com", "default_password", Role.CLIENT.name());  // Menggunakan Role.CLIENT
    }

    // Konstruktor dengan parameter untuk membuat instance MahasiswaDosen
    public MahasiswaDosen(String username, String email, String password) {
        super(username, email, password, Role.CLIENT.name());
    }

    // Menambahkan booking ke riwayat
    @Override
    public void addBookingToHistory(Booking booking) {
        bookingHistory.add(booking);  // Menggunakan metode add() untuk menambahkan booking ke riwayat
    }

    // Mendapatkan riwayat booking
    public List<Booking> getBookingHistory() {
        return bookingHistory;  // Mengembalikan daftar riwayat booking
    }
}
