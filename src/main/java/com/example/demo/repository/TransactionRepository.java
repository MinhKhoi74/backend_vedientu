package com.example.demo.repository;

import com.example.demo.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // ✅ Lấy lịch sử giao dịch của người dùng, sắp xếp theo thời gian giao dịch từ mới nhất
    List<Transaction> findByUserIdOrderByTransactionDateDesc(Long userId); // Sắp xếp theo transactionDate giảm dần (mới nhất trước)
    List<Transaction> findAllByOrderByTransactionDateDesc();

    // ✅ Lấy giao dịch theo ID và ID người dùng
    Transaction findByIdAndUserId(Long transactionId, Long userId);
}
