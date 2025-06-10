package com.example.room_reservation.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

@Entity
public class BookingManager {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @ManyToOne
    private UserBase user;  // Tambahan relasi ke UserBase

    @OneToMany(mappedBy = "bookingManager")
    private List<Booking> managedBookings;

    @OneToMany
    private List<Room> rooms;  // Daftar ruangan yang dikelola oleh BookingManager

    // Getter dan Setter untuk user
    public UserBase getUser() {
        return user;
    }

    public void setUser(UserBase user) {
        this.user = user;
    }

    // Getter dan Setter untuk id
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    // Getter dan Setter untuk name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter dan Setter untuk managedBookings
    public List<Booking> getManagedBookings() {
        return managedBookings;
    }

    public void setManagedBookings(List<Booking> managedBookings) {
        this.managedBookings = managedBookings;
    }

    // Getter dan Setter untuk rooms
    public List<Room> getRooms() {
        return rooms;
    }

    public void setRooms(List<Room> rooms) {
        this.rooms = rooms;
    }

    // Method untuk mendapatkan booking berdasarkan user
    public List<Booking> getBookingsForUser(UserBase user) {
        return managedBookings.stream()
                .filter(booking -> booking.getUser().equals(user))
                .collect(Collectors.toList());
    }

    // Method untuk mendapatkan daftar semua ruangan
    public List<Room> getAllRooms() {
        return rooms;
    }
}
