package com.example.demo.service;

import com.example.demo.dto.ReportResponse;
import com.example.demo.repository.ReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ReportService {
    @Autowired
    private ReportRepository reportRepository;

    public ReportResponse generateReport() {
        BigDecimal totalRevenue = reportRepository.getTotalRevenue();
        Long totalRides = reportRepository.getTotalRides();
        Long totalSingleTickets = reportRepository.getTotalSingleTickets();
        Long totalMonthlyTickets = reportRepository.getTotalMonthlyTickets();
        Long totalVipTickets = reportRepository.getTotalVipTickets();

        Long totalUsers = reportRepository.getTotalUsers();
        Long totalCustomers = reportRepository.getTotalCustomers();
        Long totalDrivers = reportRepository.getTotalDrivers();
        Long totalAdmins = reportRepository.getTotalAdmins();

        return new ReportResponse(
                totalRevenue != null ? totalRevenue : BigDecimal.ZERO,
                totalRides != null ? totalRides : 0,
                totalSingleTickets != null ? totalSingleTickets : 0,
                totalMonthlyTickets != null ? totalMonthlyTickets : 0,
                totalVipTickets != null ? totalVipTickets : 0,
                totalUsers != null ? totalUsers : 0,
                totalCustomers != null ? totalCustomers : 0,
                totalDrivers != null ? totalDrivers : 0,
                totalAdmins != null ? totalAdmins : 0
        );
    }
}
