package com.progect.BankingApp.service.impl;

import com.progect.BankingApp.Exception.ResourceNotFoundException;
import com.progect.BankingApp.dto.AccountDto;
import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.mapper.AccountMapper;
import com.progect.BankingApp.repositry.AccountRepository;
import com.progect.BankingApp.service.AccountService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;

    @Override
    @Transactional
    public AccountDto createAccount(AccountDto accountDto) {
        if (accountDto.getUserId() <= 0) {
            throw new IllegalArgumentException("Invalid user ID");
        }

        if (accountRepository.existsByCardNumber(accountDto.getCardNumber())) {
            throw new IllegalArgumentException("Account with this card number already exists");
        }

        Account account = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account);
        return AccountMapper.mapToAccountDto(savedAccount);
    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        return AccountMapper.mapToAccountDto(account);
    }

    @Override
    @Transactional
    public AccountDto updateAccount(Long id, AccountDto accountDto) {
        Account existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        if (accountRepository.existsByCardNumber(accountDto.getCardNumber())) {
            throw new IllegalArgumentException("Account with this card number already exists");
        }
        existingAccount.setCardNumber(accountDto.getCardNumber());
        existingAccount.setBalance(accountDto.getBalance());
        existingAccount.setUserId(accountDto.getUserId());

        Account updatedAccount = accountRepository.save(existingAccount);
        return AccountMapper.mapToAccountDto(updatedAccount);
    }

    @Override
    @Transactional
    public void deleteAccount(Long id) {
        Account account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
        accountRepository.delete(account);
    }
}