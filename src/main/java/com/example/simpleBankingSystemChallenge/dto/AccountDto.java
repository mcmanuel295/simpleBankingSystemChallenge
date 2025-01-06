package com.example.simpleBankingSystemChallenge.dto;

import lombok.Data;

import java.math.BigDecimal;

public record AccountDto(
        long id,
        String lastName,
        String firstName,
        BigDecimal balance
        ) {


}
