package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class ReportResponse {
    private BigDecimal totalRevenue;
    private Long totalRides;
    private Long totalSingleTickets;
    private Long totalMonthlyTickets;
    private Long totalVipTickets;

    private Long totalUsers;
    private Long totalCustomers;
    private Long totalDrivers;
    private Long totalAdmins;
}
