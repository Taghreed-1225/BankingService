package com.progect.BankingApp.controller;

import com.progect.BankingApp.dto.AccountDto;
import com.progect.BankingApp.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/accounts")
public class AccountController {

    private AccountService accountService;
    @Autowired
    private RestTemplate restTemplate;


    private static final String USER_SERVICE_URL = "http://localhost:8080/validateToken";
    private static final String USER_SERVICE_URL2 = "http://localhost:8080/extractUserId";

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    //Add Account REST API
    @PostMapping
    public ResponseEntity<?> addAccount(@RequestBody AccountDto accountDto , @RequestHeader("Authorization") String token)
    {
        if (!isTokenValid(token)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }
        return new ResponseEntity<>(accountService.createAccount(accountDto), HttpStatus.CREATED);
    }

    //get Account REST API
    @GetMapping("/{id}")
    public ResponseEntity<?> getAccountById(@PathVariable Long id ,  @RequestHeader("Authorization") String token){

        if (!isTokenValid(token)) {
            return new ResponseEntity<>("Invalid token", HttpStatus.UNAUTHORIZED);
        }

        // fetch account by id
        AccountDto accountDto = accountService.getAccountById(id);

        // extract userId from token
        ResponseEntity<Integer> response = getUserId(token);
        int userIdFromToken = response.getBody();

        if(userIdFromToken!=accountDto.getUserId())
        {
            return new ResponseEntity<>("Forbidden: You do not own this account", HttpStatus.FORBIDDEN);

        }


        return ResponseEntity.ok(accountDto);
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

