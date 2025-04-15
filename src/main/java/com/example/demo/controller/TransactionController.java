package com.example.demo.controller;

import com.example.demo.dto.TransactionResponse;
import com.example.demo.service.TransactionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import org.springframework.http.ResponseEntity;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;
import com.example.demo.config.JwtUtil;

@RestController
// @RequestMapping("/user")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserService userService;

    // ✅ API cho ADMIN: xem tất cả giao dịch
    @GetMapping("/transactions")
    public List<TransactionResponse> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    // ✅ API cho USER: xem tất cả giao dịch của chính mình
    @GetMapping("/user/transactions")
    public ResponseEntity<?> getTransactionsByUser(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            String email = jwtUtil.extractEmail(jwt);
            User user = userService.findByEmail(email);

            if (user == null) {
                return ResponseEntity.status(404).body("User not found");
            }

            List<TransactionResponse> transactions = transactionService.getTransactionsByUserId(user.getId());
            return ResponseEntity.ok(transactions);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Lỗi khi lấy danh sách giao dịch: " + e.getMessage());
        }
    }
    // ✅ API cho USER: xem chi tiết 1 giao dịch của chính mình
@GetMapping("/user/transactions/{transactionId}")
public ResponseEntity<?> getTransactionDetailById(
        @PathVariable Long transactionId,
        @RequestHeader("Authorization") String token) {

    try {
        String jwt = token.replace("Bearer ", "");
        String email = jwtUtil.extractEmail(jwt);
        User user = userService.findByEmail(email);

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
        }

        TransactionResponse transaction = transactionService.getTransactionByIdAndUserId(transactionId, user.getId());

        if (transaction == null) {
            return ResponseEntity.status(404).body("Giao dịch không tồn tại hoặc không thuộc về bạn");
        }

        return ResponseEntity.ok(transaction);
    } catch (Exception e) {
        return ResponseEntity.status(400).body("Lỗi khi lấy chi tiết giao dịch: " + e.getMessage());
    }
}

}
