package com.example.simpleBankingSystemChallenge.util;

import com.example.simpleBankingSystemChallenge.dto.UserDto;
import com.example.simpleBankingSystemChallenge.model.User;

public class Utils {


    public static UserDto toDto(User user){
        return new UserDto(
                user.getUserId(),
                user.getLastName(),
                user.getFirstName(),
                user.getAccountBalance());
    }


    public static User toUser(UserDto dto){
        User user = new User();

        user.setUserId(dto.id());
        user.setLastName(dto.lastName());
        user.setFirstName(dto.firstName());
        user.setAccountBalance(dto.balance());
        return user;
    }
}
