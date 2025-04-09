package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BusResponse {
    private Long id;
    private String licensePlate;
    private String route;
    private String driverName;
    private Long driverId;
}
