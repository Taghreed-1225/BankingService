package com.progect.BankingApp.service;

import com.progect.BankingApp.dto.AccountDto;
import com.progect.BankingApp.entity.Account;

import java.util.List;

public interface AccountService {
     AccountDto createAccount(AccountDto account);
     AccountDto getAccountById(Long id);
     AccountDto updateAccount(Long id, AccountDto account);
     void deleteAccount(Long id);

    List<AccountDto> getAccountsByUserId(int userId);
}
