package com.example.simpleBankingSystemChallenge.service;

import com.example.simpleBankingSystemChallenge.dto.AccountDto;
import com.example.simpleBankingSystemChallenge.model.Account;

import java.math.BigDecimal;
import java.util.List;

public interface AccountService {

    AccountDto createAccount(Account user);

    AccountDto deposit(long userId, BigDecimal amount);

    AccountDto withdraw(long userId,BigDecimal amount);

    BigDecimal balanceCheck(long userId);

    String transfer(long userId,String recipientAccount,BigDecimal amount);

    AccountDto updateUser(long userId, Account user);

    String verify(Account user);

    List<AccountDto> getAllAccount();
}
