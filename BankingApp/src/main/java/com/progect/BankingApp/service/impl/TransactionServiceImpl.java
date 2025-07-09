package com.progect.BankingApp.service.impl;

import com.progect.BankingApp.dto.TransactionDto;
import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.entity.Transaction;
import com.progect.BankingApp.entity.TransactionType;
import com.progect.BankingApp.repositry.AccountRepository;
import com.progect.BankingApp.mapper.TransactionMapper;
import com.progect.BankingApp.repositry.TransactionRepository;
import com.progect.BankingApp.service.TransactionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {

    private TransactionRepository transactionRepository;
    private AccountRepository accountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  AccountRepository accountRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
    }

    @Override
    @Transactional
    public TransactionDto deposit(Long accountId, double amount, String notes) {
        // Find account
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new RuntimeException("Account not found"));

        // Validate amount
        if (amount <= 0) {
            throw new RuntimeException("Amount must be positive");
        }

        // Update account balance
        account.setBalance(account.getBalance() + amount);
        Account savedAccount = accountRepository.save(account);

        // Create transaction record
        Transaction transaction = Transaction.builder()
                .account(savedAccount)
                .type(TransactionType.DEPOSIT)
                .amount(amount)
                .notes(notes)
                .transactionDate(LocalDateTime.now())
                .balanceAfter(savedAccount.getBalance())
                .build();

        Transaction savedTransaction = transactionRepository.save(transaction);
        return TransactionMapper.mapToTransactionDto(savedTransaction);
    }

    @Override
    @Transactional
    public TransactionDto withdraw(Long accountId, double amount, String notes) {
            // Find account
            Account account = accountRepository.findById(accountId)
                    .orElseThrow(() -> new RuntimeException("Account not found"));

            // Validate amount
            if (amount <= 0) {
                throw new RuntimeException("Amount must be positive");
            }

            // Check sufficient balance
            if (account.getBalance() < amount) {
                throw new RuntimeException("Insufficient balance");
            }

            // Update account balance
            account.setBalance(account.getBalance() - amount);
            Account savedAccount = accountRepository.save(account);

            // Create transaction record
            Transaction transaction = Transaction.builder()
                    .account(savedAccount)
                    .type(TransactionType.WITHDRAWA)
                    .amount(amount)
                    .notes(notes)
                    .transactionDate(LocalDateTime.now())
                    .balanceAfter(savedAccount.getBalance())
                    .build();

            Transaction savedTransaction = transactionRepository.save(transaction);
            return TransactionMapper.mapToTransactionDto(savedTransaction);
    }

    @Override
    public List<TransactionDto> getTransactionHistory(Long accountId) {
        List<Transaction> transactions = transactionRepository.findByAccountIdOrderByTransactionDateDesc(accountId);
        return transactions.stream()
                .map(TransactionMapper::mapToTransactionDto)
                .collect(Collectors.toList());
    }
}
