package com.example.demo.repository;

import com.example.demo.entity.Ticket;
import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Long> {
    // Cũ: Không có sắp xếp
    List<Ticket> findByUser(User user);

    // ✅ Mới: Trả danh sách vé theo user và sắp xếp giảm dần theo thời gian mua
    List<Ticket> findByUserOrderByPurchaseDateDesc(User user);

    Optional<Ticket> findByQrCode(String qrCode);
}
