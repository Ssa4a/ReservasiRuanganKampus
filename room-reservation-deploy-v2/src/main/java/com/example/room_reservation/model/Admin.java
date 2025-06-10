package com.example.room_reservation.model;

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("ADMIN")  // Pastikan nilai ini tetap "ADMIN"
public class Admin extends UserBase {  // Ganti nama kelas dari UserAdmin menjadi Admin

    // Konstruktor default tanpa parameter (dibutuhkan oleh JPA)
    public Admin() {
        super("default_username", "default_email@example.com", "default_password", Role.ADMIN.name());  // Menggunakan Role.ADMIN.name() untuk konsistensi
    }

    // Konstruktor dengan parameter
    public Admin(String username, String email, String password) {
        super(username, email, password, Role.ADMIN.name());  // Menggunakan Role.ADMIN.name() untuk konsistensi
    }

    // Implementasi metode addBookingToHistory di Admin
    @Override
    public void addBookingToHistory(Booking booking) {
        // Implementasi kosong, karena Admin tidak menyimpan riwayat booking
    }
}
