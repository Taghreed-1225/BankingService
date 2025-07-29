package com.progect.BankingApp.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:4300"})
@RestController
@RequestMapping("bank/token/validation")
public class ValidationController {
    @GetMapping
    public ResponseEntity<String> validateToken(){
        return ResponseEntity.ok("success");
    }
}
