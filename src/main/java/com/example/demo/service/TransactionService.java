package com.example.demo.service;

import com.example.demo.dto.TransactionResponse;
import com.example.demo.entity.Transaction;
import com.example.demo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public List<TransactionResponse> getAllTransactions() {
        List<Transaction> transactions = transactionRepository.findAll();

        return transactions.stream().map(t -> TransactionResponse.builder()
                .id(t.getId())
                .userFullName(t.getUser().getFullName())
                .ticketType(t.getTicket().getTicketType().name()) // assuming Ticket has ticketType enum
                .amount(t.getAmount())
                .transactionDate(t.getTransactionDate())
                .paymentMethod(t.getPaymentMethod().name())
                .status(t.getStatus().name())
                .build()
        ).collect(Collectors.toList());
    }

    public List<TransactionResponse> getTransactionsByUserId(Long userId) {
        List<Transaction> transactions = transactionRepository.findByUserId(userId);

        return transactions.stream().map(t -> TransactionResponse.builder()
                .id(t.getId())
                .userFullName(t.getUser().getFullName())
                .ticketType(t.getTicket().getTicketType().name())
                .amount(t.getAmount())
                .transactionDate(t.getTransactionDate())
                .paymentMethod(t.getPaymentMethod().name())
                .status(t.getStatus().name())
                .build()
        ).collect(Collectors.toList());
    }
    public TransactionResponse getTransactionByIdAndUserId(Long transactionId, Long userId) {
        Transaction transaction = transactionRepository.findByIdAndUserId(transactionId, userId);
        if (transaction == null) return null;
    
        return TransactionResponse.builder()
                .id(transaction.getId())
                .userFullName(transaction.getUser().getFullName())
                .ticketType(transaction.getTicket().getTicketType().name())
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .paymentMethod(transaction.getPaymentMethod().name())
                .status(transaction.getStatus().name())
                .build();
    }
    
}
