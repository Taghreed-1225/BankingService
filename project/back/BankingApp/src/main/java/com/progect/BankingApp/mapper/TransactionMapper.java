package com.progect.BankingApp.mapper;

import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.entity.Transaction;
import com.progect.BankingApp.entity.TransactionType;
import com.progect.BankingApp.model.transaction.TransactionRequest;
import com.progect.BankingApp.model.transaction.TransactionResponseDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
@Component
public class TransactionMapper {
    // تحويل Transaction إلى TransactionResponseDTO
    public TransactionResponseDTO mapToTransactionResponseDTO(Transaction transaction) {
        return new TransactionResponseDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getType(),
                transaction.getCreated_at(),
                transaction.getPayment_method()
        );
    }

    // تحويل TransactionRequest إلى Transaction (عام)
    public static Transaction mapToTransaction(TransactionRequest request, Account account, TransactionType type) {
        double newBalance = type == TransactionType.DEPOSIT
                ? account.getBalance() + request.getAmount()
                : account.getBalance() - request.getAmount();

        return Transaction.builder()
                .account(account)
                .amount(request.getAmount())
                .type(type)
                .transactionDate(LocalDateTime.now())
                .created_at(LocalDateTime.now())
                .payment_method("credit card")
                .balanceAfter(newBalance)
                .build();
    }
}
