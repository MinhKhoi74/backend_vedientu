package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusRequest {
    private String licensePlate;
    private String model;
    private int capacity;
    private String route;
    private Long driverId; // ✅ quan trọng
}

