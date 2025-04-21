package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "buses")
public class Bus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String licensePlate;

    private String model;
    private int capacity;
    private String route;

    @OneToOne
    @JoinColumn(name = "driver_id", referencedColumnName = "id")
    @JsonBackReference
    private User driver;

}
