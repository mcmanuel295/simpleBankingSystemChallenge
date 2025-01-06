package com.example.simpleBankingSystemChallenge.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

@Entity
@Table(name = "Account")
public final class Account{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotBlank(message = "cannot be blank")
    private String firstName;

    @NotBlank(message = "cannot be blank")
    private String lastName;

    @NotBlank(message = "cannot be null")
    @NotNull(message = "cannot be null")
    private String username;

    private String fullName;

    @Column(length = 10,unique = true)
    private String accountNumber;

    @Min(value = 0,message = "amount cannot be less than zero")
    private BigDecimal accountBalance= BigDecimal.ZERO;

    private String password;

    @Column(nullable = false)
    private Role role ;

    private Account_Type type;

    public Account() {
    }


    public Long getUserId() {
               return userId;
    }

    public void setUserId(Long userId) {

        this.userId = userId;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Account_Type getType() {
        return type;
    }

    public void setType(Account_Type type) {
        this.type = type;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
        fullName();
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
        fullName();
    }


    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public BigDecimal getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(BigDecimal accountBalance) {
        this.accountBalance = accountBalance;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public void fullName(){
        setFullName(getLastName()+" "+getFirstName());
    }
}
