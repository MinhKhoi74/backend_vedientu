package com.example.demo.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {
    private Long id;
    private String userFullName;
    private String ticketType;
    private BigDecimal amount;
    private LocalDateTime transactionDate;
    private String paymentMethod;
    private String status;
}
