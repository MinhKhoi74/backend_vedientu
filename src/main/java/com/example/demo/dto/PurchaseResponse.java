package com.example.demo.dto;

import com.example.demo.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseResponse {
    private String message;
    private Long ticketId;
    private double amount;
    private Transaction.PaymentMethod paymentMethod; // Sửa kiểu dữ liệu từ String thành Enum
}
