package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ReportRepository extends JpaRepository<Transaction, Long> {

    // Tổng doanh thu từ giao dịch hoàn thành
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.status = 'COMPLETED'")
    BigDecimal getTotalRevenue();

    // Tổng lượt quét vé hợp lệ
    @Query("SELECT COUNT(r.id) FROM RideLog r WHERE r.status = 'VALID'")
    Long getTotalRides();

    // Tổng số vé loại SINGLE
    @Query("SELECT COUNT(t.id) FROM Ticket t WHERE t.ticketType = 'SINGLE'")
    Long getTotalSingleTickets();

    // Tổng số vé loại MONTHLY
    @Query("SELECT COUNT(t.id) FROM Ticket t WHERE t.ticketType = 'MONTHLY'")
    Long getTotalMonthlyTickets();

    // Tổng số vé loại VIP
    @Query("SELECT COUNT(t.id) FROM Ticket t WHERE t.ticketType = 'VIP'")
    Long getTotalVipTickets();

    // Tổng người dùng
    @Query("SELECT COUNT(u.id) FROM User u")
    Long getTotalUsers();

    // Tổng CUSTOMER
    @Query("SELECT COUNT(u.id) FROM User u WHERE u.role = 'CUSTOMER'")
    Long getTotalCustomers();

    // Tổng DRIVER
    @Query("SELECT COUNT(u.id) FROM User u WHERE u.role = 'DRIVER'")
    Long getTotalDrivers();

    // Tổng ADMIN
    @Query("SELECT COUNT(u.id) FROM User u WHERE u.role = 'ADMIN'")
    Long getTotalAdmins();
}
