package com.example.simpleBankingSystemChallenge.service;

import com.example.simpleBankingSystemChallenge.dto.UserDto;
import com.example.simpleBankingSystemChallenge.model.User;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

public interface UserService {

    UserDto createAccount(User user);

    UserDto withdraw(long userId,BigDecimal amount);

    BigDecimal balanceCheck(long userId);

    String transfer(long userId,String recipientAccount,BigDecimal amount);

    UserDto updateUser(long userId,User user);

    String verify(User user);
}
