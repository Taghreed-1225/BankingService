package com.progect.BankingApp.repositry;

import com.progect.BankingApp.entity.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {


    boolean existsByCardNumber(String cardNumber);
}
