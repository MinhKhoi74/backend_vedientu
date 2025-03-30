package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "ride_log")
public class RideLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user; // Hành khách sử dụng vé

    @ManyToOne
    @JoinColumn(name = "ticket_id", nullable = false)
    private Ticket ticket; // Vé được sử dụng

    public enum Status {
        VALID, INVALID
    }

    @ManyToOne
    @JoinColumn(name = "driver_id") // Tài xế quét vé
    private User driver;

    private String route; // Lộ trình từ đâu đến đâu

    private LocalDateTime rideTime;

    @Enumerated(EnumType.STRING)
    private Status status; // VALID / INVALID
}
