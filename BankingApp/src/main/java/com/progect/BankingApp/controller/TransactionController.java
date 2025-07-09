package com.progect.BankingApp.controller;

import com.progect.BankingApp.dto.AccountDto;
import com.progect.BankingApp.dto.TransactionDto;
import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.service.AccountService;
import com.progect.BankingApp.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String USER_SERVICE_URL = "http://localhost:8080/validateToken";
    private static final String USER_SERVICE_URL2 = "http://localhost:8080/extractUserId";

    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @Autowired
    private AccountService accountService;

    // Deposit money
    @PostMapping("/deposit")
    public ResponseEntity<?> deposit(
            @RequestParam Long accountId,
            @RequestParam double amount,
            @RequestParam(required = false) String notes,
            @RequestHeader("Authorization") String token) {

        if (!isTokenValid(token)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        AccountDto accountDto= accountService.getAccountById(accountId);
        // Check userId ownership
        ResponseEntity<Integer> response = getUserId(token);
        int userIdFromToken = response.getBody();
        if (userIdFromToken != accountDto.getUserId()) {
            return new ResponseEntity<>("You are not authorized to access this account", HttpStatus.UNAUTHORIZED);
        }

        TransactionDto transaction = transactionService.deposit(accountId, amount, notes) ;
        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // Withdraw money
    @PostMapping("/withdraw")
    public ResponseEntity<?> withdraw(
            @RequestParam Long accountId,
            @RequestParam double amount,
            @RequestParam(required = false) String notes,
            @RequestHeader("Authorization") String token) {

        if (!isTokenValid(token)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        TransactionDto transaction = transactionService.withdraw(accountId, amount, notes);


        AccountDto accountDto= accountService.getAccountById(accountId);
        // Check userId ownership
        ResponseEntity<Integer> response = getUserId(token);
        int userIdFromToken = response.getBody();
        if (userIdFromToken != accountDto.getUserId()) {
            return new ResponseEntity<>("You are not authorized to access this account", HttpStatus.UNAUTHORIZED);
        }

        return new ResponseEntity<>(transaction, HttpStatus.CREATED);
    }

    // Get transaction history
    @GetMapping("/history/{accountId}")
    public ResponseEntity<List<TransactionDto>> getTransactionHistory(@PathVariable Long accountId, @RequestHeader("Authorization") String token) {

//        if (!isTokenValid(token)) {
//           // return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
//        }
//
        List<TransactionDto> transactions = transactionService.getTransactionHistory(accountId);
        return ResponseEntity.ok(transactions);
    }


    public boolean isTokenValid(String token) {
        System.out.println("is token valid in todo controller");
        HttpHeaders headers = new HttpHeaders();
        System.out.println("1");
        headers.set("Authorization", token);
        System.out.println("2");// أو Bearer + token لو مطلوب
        HttpEntity<String> entity = new HttpEntity<>(headers);
        System.out.println("3");

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    USER_SERVICE_URL,
                    HttpMethod.POST,
                    entity,
                    String.class


            );
            System.out.println("4");
            System.out.println("Response body: " + response.getBody());

            return "valid token".equalsIgnoreCase(response.getBody());
        }

        catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("5");
            System.out.println("Status Code: " + e.getStatusCode());
            System.out.println("Error Body: " + e.getResponseBodyAsString());
            return false;
        }

    }

    ResponseEntity<Integer> getUserId(String token)
    {
        System.out.println("is token valid in ser id");
        HttpHeaders headers = new HttpHeaders();
        System.out.println("1");
        headers.set("Authorization", token);
        System.out.println("2");// أو Bearer + token لو مطلوب
        HttpEntity<String> entity = new HttpEntity<>(headers);
        System.out.println("3");
        try {
            ResponseEntity<Integer> response = restTemplate.exchange(
                    USER_SERVICE_URL2,
                    HttpMethod.POST,
                    entity,
                    Integer.class


            );
            System.out.println("4");
            System.out.println("Response body: " + response.getBody());

            return response ;
        }

        catch (HttpClientErrorException | HttpServerErrorException e) {
            System.out.println("5");
            System.out.println("Status Code: " + e.getStatusCode());
            System.out.println("Error Body: " + e.getResponseBodyAsString());
            return ResponseEntity.status(e.getStatusCode()).body(0);
        }
    }
}
