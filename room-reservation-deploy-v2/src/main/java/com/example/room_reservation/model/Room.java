package com.example.room_reservation.model;

import jakarta.persistence.*;
import java.util.List;  // Menambahkan impor untuk List

@Entity
public class Room {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;  // Nama ruangan, contoh: "Kelas A", "Lab 1", "Seminar 1"
    private String type;  // Jenis ruangan: "Kelas", "Lab", "Seminar"
    @Enumerated(EnumType.STRING)  // Menyimpan status sebagai string di database
    private RoomStatus status;

    @OneToMany(mappedBy = "room")
    private List<Booking> bookings;  // Daftar booking terkait dengan ruangan ini

    // Getter dan Setter
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public List<Booking> getBookings() {
        return bookings;
    }

    public RoomStatus getStatus() {
        return status;
    }

    public void setStatus(RoomStatus status) {
        this.status = status;
    }

    public void setBookings(List<Booking> bookings) {
        this.bookings = bookings;
}
}
