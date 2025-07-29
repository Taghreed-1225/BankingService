package com.progect.BankingApp.service.impl;

import com.progect.BankingApp.Exception.ConflictException;
import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.mapper.AccountMapper;
import com.progect.BankingApp.model.account.AccountRequestDTO;
import com.progect.BankingApp.model.auth.AuthenticationRequest;
import com.progect.BankingApp.model.auth.AuthenticationResponse;
import com.progect.BankingApp.repositry.AccountRepository;
import com.progect.BankingApp.security.JWTService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthenticationServiceImpl  {

    private final AuthenticationManager authenticationManager;
    private final AccountRepository accountRepository;
    private final JWTService jwtService;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        log.info("user wants to login with this credentials {}", authenticationRequest);
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()
                )
        );

        Account userAccount = accountRepository.findByEmail(authenticationRequest.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found"));

        var jwtToken = jwtService.generateToken(authentication);
        log.info("The user has login successfully");
        return AuthenticationResponse.builder()
                .token(jwtToken)
                .id(userAccount.getId())
                .email(userAccount.getEmail())
                .name(userAccount.getName())
                .build();
    }


    public AuthenticationResponse register(AccountRequestDTO accountRequestDTO) {
        Account userAccount = accountMapper.toEntity(accountRequestDTO);

        String cardNumber = generateCardNumber();
        userAccount.setCardNumber(cardNumber);
        log.info("This Card Number for The new Account {}", cardNumber);
        userAccount.setPassword(passwordEncoder.encode(userAccount.getPassword()));
        log.info("This is UserAccount {}", userAccount);

        if(accountRepository.findByEmail(accountRequestDTO.getEmail()).isPresent()){
            log.error("this Account already exist");
            throw new ConflictException("this Account already exist");
        }

        accountRepository.save(userAccount);
        log.info("Account Added Successfully {}", userAccount);
        return authenticate(new AuthenticationRequest(accountRequestDTO.getEmail(), accountRequestDTO.getPassword()));
    }

    private static String generateCardNumber() {
        UUID uuid = UUID.randomUUID();
        String cardNumber = String.valueOf(Math.abs(uuid.getMostSignificantBits()));

        int firstDigit = Character.getNumericValue(cardNumber.charAt(0));
        int newFirstDigit = (firstDigit % 9) + 1;
        cardNumber = newFirstDigit + cardNumber.substring(1);

        if (cardNumber.startsWith("-")) {
            cardNumber = cardNumber.substring(1); // Remove leading '-'
        }

        return cardNumber.substring(0, 16);
    }
}
