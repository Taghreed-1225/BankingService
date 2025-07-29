package com.progect.BankingApp.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "transactions")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "account_id" , nullable = false)
    private Account account;

    @Column(nullable = false)
    @PositiveOrZero(message = "Amount must be positive or zero")
    private double amount;

    private String notes;

    @Column(name = "transaction_date")
    private LocalDateTime transactionDate;


    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private TransactionType type;

    private String payment_method = "credit card";

    private LocalDateTime created_at;

    private double balanceAfter;
}
