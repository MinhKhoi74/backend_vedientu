package com.example.demo.repository;

import com.example.demo.entity.QrCode;
import com.example.demo.entity.Ticket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface QrCodeRepository extends JpaRepository<QrCode, Long> {
    Optional<QrCode> findByTicket(Ticket ticket);
}
