package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "user_id", nullable = true)
    @JsonBackReference(value = "user-rideLogs")
    private User user; // Hành khách sử dụng vé

    @Column(name = "ticket_id", nullable = true)
    private Long ticketId; // ID của vé đã sử dụng

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "driver_id", nullable = true)
    @JsonBackReference(value = "driver-rideLogs")
    private User driver; // Tài xế quét vé

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "bus_id")
    private Bus bus;

    @Column(name = "route")
    private String route;

    private LocalDateTime rideTime;

    @Enumerated(EnumType.STRING)
    private Status status; // VALID / INVALID

    private String userName;

    private String busCode;

    public enum Status {
        VALID, INVALID
    }
}
