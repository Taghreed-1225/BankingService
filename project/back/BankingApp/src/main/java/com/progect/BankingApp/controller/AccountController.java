package com.progect.BankingApp.controller;

import com.progect.BankingApp.common.ApiResponse;

import com.progect.BankingApp.model.transaction.TransactionResponseDTO;
import com.progect.BankingApp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@AllArgsConstructor
public class AccountController {

    private final AccountService accountService;


    @GetMapping("balance")
    public ResponseEntity<Double> viewCurrentAccountBalance(){
        return new ResponseEntity<>(accountService.viewAuthenticatedAccountBalance(), HttpStatus.OK);
    }

    @GetMapping("transactions")
    public ResponseEntity<List<TransactionResponseDTO>> viewCurrentAccountTransactions(){
        return new ResponseEntity<>(accountService.viewAuthenticatedAccountTransactions(), HttpStatus.OK);
    }

    @GetMapping("cardNumber")
    public ResponseEntity<String> getCurrentAccountCardNumber(){
        return ResponseEntity.ok(accountService.getAuthenticatedAccountCardNumber());
    }

}