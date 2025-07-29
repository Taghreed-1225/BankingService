package com.progect.BankingApp.controller;

import com.progect.BankingApp.common.ApiResponse;
import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.service.AccountService;
import com.progect.BankingApp.service.TransactionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import com.progect.BankingApp.model.transaction.TransactionRequest;

import java.util.List;
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/transactions")
public class TransactionController {



    private final TransactionService transactionService;


    @PostMapping("deposit")
    public ResponseEntity<String> deposit(@Valid @RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.deposit(transactionRequest), HttpStatus.CREATED);
    }

    @PostMapping("withdraw")
    public ResponseEntity<String> withdraw(@Valid @RequestBody TransactionRequest transactionRequest){
        return new ResponseEntity<>(transactionService.withdraw(transactionRequest), HttpStatus.ACCEPTED);
    }
}