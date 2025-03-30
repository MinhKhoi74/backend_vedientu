package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface ReportRepository extends JpaRepository<Transaction, Long> {

    // Tính tổng doanh thu từ giao dịch hoàn thành
    @Query("SELECT COALESCE(SUM(t.amount), 0) FROM Transaction t WHERE t.status = 'COMPLETED'")
    BigDecimal getTotalRevenue();

    // Đếm tổng số lượt quét vé hợp lệ
    @Query("SELECT COUNT(r.id) FROM RideLog r WHERE r.status = 'VALID'")
    Long getTotalRides();

    // Đếm tổng số vé loại SINGLE
    @Query("SELECT COUNT(t.id) FROM Ticket t WHERE t.ticketType = 'SINGLE'")
    Long getTotalSingleTickets();

    // Đếm tổng số vé loại MONTHLY
    @Query("SELECT COUNT(t.id) FROM Ticket t WHERE t.ticketType = 'MONTHLY'")
    Long getTotalMonthlyTickets();
}
