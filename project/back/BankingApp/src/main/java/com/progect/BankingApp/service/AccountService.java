package com.progect.BankingApp.service;


import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.model.transaction.TransactionResponseDTO;

import java.util.List;

public interface AccountService {
    Double viewAuthenticatedAccountBalance();

    List<TransactionResponseDTO> viewAuthenticatedAccountTransactions();

    String getAuthenticatedAccountCardNumber();
}
