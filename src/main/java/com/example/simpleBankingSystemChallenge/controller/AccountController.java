package com.example.simpleBankingSystemChallenge.controller;

import com.example.simpleBankingSystemChallenge.dto.AccountDto;
import com.example.simpleBankingSystemChallenge.model.Account;
import com.example.simpleBankingSystemChallenge.service.serviceImp.AccountServiceImp;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/api/accounts/")
public class AccountController {
    @Autowired
    HttpServletRequest http ;

    @Autowired
    AccountServiceImp service;


    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody Account user){
        var token = service.verify(user);
        System.out.println("Token for "+user.getUsername()+" generated: "+token);

        return  new ResponseEntity<>(token,HttpStatus.ACCEPTED);
    }

    @PostMapping("/")
    public ResponseEntity<AccountDto> createAccount(@RequestBody Account newUser) {

        AccountDto user = service.createAccount(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<AccountDto> deposit(@PathVariable long userId,@RequestBody BigDecimal amount){
        AccountDto savedUsers = service.deposit(userId,amount);
        return new ResponseEntity<>(savedUsers,HttpStatus.OK);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<AccountDto> withdrawTransaction(@RequestBody Long userId,@RequestBody BigDecimal amount) {

        AccountDto savedUser = service.withdraw(userId, amount);
        return new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
    }

    @GetMapping("/{userId}/balance")
    public ResponseEntity<BigDecimal> balanceCheck(@PathVariable long userId) {
        BigDecimal balance = service.balanceCheck(userId);

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PostMapping("/{userId}/transfer")
    public ResponseEntity<String> transferTransaction(long userId, String account, BigDecimal amount) {
        String status =service.transfer(userId,account,amount);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PutMapping("/{userId}")
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('WORKER')")
    public ResponseEntity<AccountDto> update(long userId,@RequestBody Account user){
        AccountDto savedUser =service.updateUser(userId,user);
        return new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
    }

    @GetMapping("/")
    @PreAuthorize("HasAuthority('ADMIN')")
    public List<AccountDto> getUsers(){
        System.out.println("in the users method");
        return service.getAllAccount();
    }
}