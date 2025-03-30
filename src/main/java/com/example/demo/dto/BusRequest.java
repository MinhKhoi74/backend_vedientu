package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusRequest {
    private String licensePlate;
    private String route;
    private Long driverId;
}
