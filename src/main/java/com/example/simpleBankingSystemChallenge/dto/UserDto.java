package com.example.simpleBankingSystemChallenge.dto;

import lombok.Data;

import java.math.BigDecimal;

public record UserDto(
        long id,
        String lastName,
        String firstName,
        BigDecimal balance
        ) {


}
