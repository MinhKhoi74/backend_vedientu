package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    List<Ticket> findByUser(User user); // ✅ Truy vấn danh sách vé theo User

    Optional<Ticket> findByQrCode(String qrCode); // ✅ Tìm vé theo mã QR
}
