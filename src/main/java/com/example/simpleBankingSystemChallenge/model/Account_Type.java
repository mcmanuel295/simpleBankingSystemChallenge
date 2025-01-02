package com.example.simpleBankingSystemChallenge.model;

public enum Account_Type {

    ZERO("zero"),
    NON_ZERO("non_zero");

    private String type;

    Account_Type(String type) {
        this.type = type;
    }
}
