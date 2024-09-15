package com.example.bookingDemo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDate;

@Entity
@Table(name = "bookings")
@Data
public class Booking {
    @Id
    private Long id;

    //Many of the current entity are associated with one instance of another entity
    //many bookings belong to one user
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private LocalDate bookingDate;

    @Column(nullable = false)
    private String bookingItem;

    @Column(nullable = false)
    private String status;
}
