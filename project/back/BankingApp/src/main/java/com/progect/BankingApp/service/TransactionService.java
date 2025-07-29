package com.progect.BankingApp.service;


import com.progect.BankingApp.model.transaction.TransactionRequest;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

public interface TransactionService {
    String deposit(TransactionRequest transactionRequest);

    String withdraw(TransactionRequest transactionRequest);


}
