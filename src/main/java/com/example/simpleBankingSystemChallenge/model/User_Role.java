package com.example.simpleBankingSystemChallenge.model;

public enum User_Role {

    OWNER ("owner"),
    ADMIN("admin"),
    WORKER("worker"),
    USER("user"),
    ;

    private final String role;
    User_Role(String role) {
        this.role=role;
    }
}
