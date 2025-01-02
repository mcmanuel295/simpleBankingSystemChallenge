package com.example.simpleBankingSystemChallenge.controller;

import com.example.simpleBankingSystemChallenge.dto.UserDto;
import com.example.simpleBankingSystemChallenge.model.User;
import com.example.simpleBankingSystemChallenge.service.serviceImp.UserServiceImp;
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
public class UserController {
    @Autowired
    HttpServletRequest http ;

    @Autowired
    UserServiceImp service;

    @GetMapping("/users")
//    @PreAuthorize("Has authority")
    public List<UserDto> getUsers(){
        System.out.println("in the users method");
        CsrfToken token= (CsrfToken)http.getAttribute("_csrf");
        System.out.println(token.getToken());
        return service.getAllUser();
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User user){
        var token = service.verify(user);
        System.out.println("Token for "+user.getUsername()+" generated: "+token);

        return  new ResponseEntity<>(token,HttpStatus.ACCEPTED);
    }

    @PostMapping("/register")
    public ResponseEntity<UserDto> createAccount(@RequestBody User newUser) {

        UserDto user = service.createAccount(newUser);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/withdraw")
    public ResponseEntity<UserDto> withdrawTransaction(@RequestBody Long userId,@RequestBody BigDecimal amount) {

        UserDto savedUser = service.withdraw(userId, amount);
        return new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
    }

    @GetMapping("/balance")
    public ResponseEntity<BigDecimal> balanceCheck(@PathVariable long userId) {
        BigDecimal balance = service.balanceCheck(userId);

        return new ResponseEntity<>(balance, HttpStatus.OK);
    }

    @PostMapping("/transfer")
    public ResponseEntity<String> transferTransaction(long userId, String account, BigDecimal amount) {
        String status =service.transfer(userId,account,amount);
        return new ResponseEntity<>(status, HttpStatus.OK);
    }

    @PostMapping("/updateUser")
    @PreAuthorize("hasRole('ADMIN') hasRole('WORKER')")
    public ResponseEntity<UserDto> update(long userId,@RequestBody User user){
        UserDto savedUser =service.updateUser(userId,user);
        return new ResponseEntity<>(savedUser, HttpStatus.ACCEPTED);
    }
}