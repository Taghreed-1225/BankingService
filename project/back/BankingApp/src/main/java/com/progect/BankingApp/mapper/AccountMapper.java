package com.progect.BankingApp.mapper;



import com.progect.BankingApp.entity.Account;
import com.progect.BankingApp.model.account.AccountRequestDTO;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    public Account toEntity(AccountRequestDTO accountRequestDTO) {
        if (accountRequestDTO == null) {
            return null;
        }

        Account account = new Account();
        account.setName(accountRequestDTO.getName());
        account.setEmail(accountRequestDTO.getEmail());
        account.setPassword(accountRequestDTO.getPassword());
        // cardNumber will be ignored and set separately

        return account;
    }

    public AccountRequestDTO toDTO(Account account) {
        if (account == null) {
            return null;
        }

        AccountRequestDTO dto = new AccountRequestDTO();
        dto.setName(account.getName());
        dto.setEmail(account.getEmail());
        dto.setPassword(account.getPassword());

        return dto;
    }
}
