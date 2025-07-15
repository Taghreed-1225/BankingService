package com.progect.BankingApp.service;

import com.progect.BankingApp.dto.TransactionDto;

import java.util.List;

public interface TransactionService {
    TransactionDto deposit(Long accountId, double amount, String notes);

    TransactionDto withdraw(Long accountId, double amount, String notes);

    List<TransactionDto> getTransactionHistory(Long accountId);
}
