package com.example.simpleBankingSystemChallenge.util;

import com.example.simpleBankingSystemChallenge.dto.AccountDto;
import com.example.simpleBankingSystemChallenge.model.Account;

public class Utils {


    public static AccountDto toDto(Account user){
        return new AccountDto(
                user.getUserId(),
                user.getLastName(),
                user.getFirstName(),
                user.getAccountBalance());
    }


    public static Account toUser(AccountDto dto){
        Account user = new Account();

        user.setUserId(dto.id());
        user.setLastName(dto.lastName());
        user.setFirstName(dto.firstName());
        user.setAccountBalance(dto.balance());
        return user;
    }
}
