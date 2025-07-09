package com.progect.BankingApp.mapper;


import com.progect.BankingApp.dto.AccountDto;
import com.progect.BankingApp.entity.Account;

public class AccountMapper {

    public static Account mapToAccount(AccountDto accountDto) {
        Account account =Account.builder()
                .id(accountDto.getId())
                .cardNumber(accountDto.getCardNumber())
                .name(accountDto.getName())
                .balance(accountDto.getBalance())
                .userId(accountDto.getUserId())
                .build();

        return account;
    }


    public static AccountDto mapToAccountDto(Account account) {
        AccountDto accountDto = new AccountDto(
                account.getId(),
                account.getCardNumber(),
                account.getName(),
                account.getBalance(),
                account.getUserId()
        );
        return accountDto;
    }
}
