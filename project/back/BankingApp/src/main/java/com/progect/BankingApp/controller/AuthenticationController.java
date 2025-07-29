package com.progect.BankingApp.controller;

import com.progect.BankingApp.model.account.AccountRequestDTO;
import com.progect.BankingApp.model.auth.AuthenticationRequest;
import com.progect.BankingApp.model.auth.AuthenticationResponse;
import com.progect.BankingApp.service.impl.AuthenticationServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4300"})
@RestController
@RequestMapping("/rest/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationServiceImpl authenticationService;

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> login(@Valid @RequestBody AuthenticationRequest authenticationRequest){
        AuthenticationResponse response = authenticationService.authenticate(authenticationRequest);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody AccountRequestDTO accountRequestDTO){
        AuthenticationResponse response = authenticationService.register(accountRequestDTO);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/hello")
    public String test(){
        return "hello" ;
    }
}
