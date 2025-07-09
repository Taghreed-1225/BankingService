package com.progect.BankingApp.service.impl;

import com.progect.BankingApp.dto.AccountDto;
import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.mapper.AccountMapper;
import com.progect.BankingApp.repositry.AccountRepository;
import com.progect.BankingApp.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {

    private AccountRepository accountRepository;


    // constructor Injection and we can ignore it because it is single bean(automaticly inject)

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public AccountDto createAccount(AccountDto accountDto) {
        Account account1 = AccountMapper.mapToAccount(accountDto);
        Account savedAccount = accountRepository.save(account1);
        return AccountMapper.mapToAccountDto(savedAccount);



    }

    @Override
    public AccountDto getAccountById(Long id) {
        Account account=accountRepository
                .findById(id)
                .orElseThrow(() -> new RuntimeException("Account does not exists ") );
        return AccountMapper.mapToAccountDto(account);
    }
}
