package com.example.simpleBankingSystemChallenge.service.serviceImp;

import com.example.simpleBankingSystemChallenge.dto.AccountDto;
import com.example.simpleBankingSystemChallenge.exception.OurException;
import com.example.simpleBankingSystemChallenge.model.Account;
import com.example.simpleBankingSystemChallenge.repository.AccountRepository;
import com.example.simpleBankingSystemChallenge.service.AccountService;
import com.example.simpleBankingSystemChallenge.service.JwtService;
import com.example.simpleBankingSystemChallenge.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;

@Service
public class AccountServiceImp implements AccountService {

    private final AccountRepository repo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    //  Constructor
    @Autowired
    public AccountServiceImp(AccountRepository repo, AuthenticationManager authenticationManager,JwtService service) {
        this.repo = repo;
        this.authenticationManager =authenticationManager;
        this.jwtService =service;
    }


    //  Create new User Account
    @Override
    public AccountDto createAccount(Account user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        user.setPassword(encoder.encode(user.getPassword()));

       Account savedUser = repo.save(user);
       return Utils.toDto(savedUser);
    }


    @Override
    public String verify(Account user) {
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user);
        }
        return "failed";
    }



//    deposit method
    @Override
    public AccountDto deposit(long userId, BigDecimal amount) {
        try {
            Account user = repo.findById(userId).orElseThrow(()-> new OurException("User Not Found!!!"));
            user.setAccountBalance( user.getAccountBalance().add(amount));

            return Utils.toDto(user);

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }


    //    Withdraw from user account
    @Override
    public AccountDto withdraw(long userId, BigDecimal amount) {
        try {

            Account savedUser = repo.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
            BigDecimal balance = savedUser.getAccountBalance();

            if (balance.subtract(amount).compareTo(BigDecimal.ZERO) < 0) {
                try {
                    throw new OurException("insufficient fund!!!");
                } catch (OurException e) {
                    throw new RuntimeException(e);
                }
            }

            balance = balance.subtract(amount);
            savedUser.setAccountBalance(balance);
            return Utils.toDto(savedUser);

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }


    // Withdraw method
    @Override
    public BigDecimal balanceCheck(long userId) {

        try {
            Account savedUser= repo.findById(userId).orElseThrow(() -> new OurException("User Not Found!!!"));
            return savedUser.getAccountBalance();

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String transfer(long userId, String recipientAccount, BigDecimal amount) {

        try {
            Account recipient = repo.findByAccountNumber(recipientAccount).orElseThrow(() -> new OurException("Recipient Account Not Found!!!"));

            deposit(recipient.getUserId(),amount);
            AccountDto dto = withdraw(userId, amount);

            return dto+" transferred "+amount+" to " +recipientAccount;

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AccountDto updateUser(long userId,Account user) {
        Account savedUser = null;
        try {
            savedUser = repo.findById(userId).orElseThrow(()-> new OurException("User Not Found!!"));
            repo.save(user);
            return  Utils.toDto(savedUser);

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<AccountDto> getAllAccount() {
        return repo.findAll()
                .stream()
                .map(user->
                   new AccountDto(user.getUserId(), user.getLastName(), user.getFirstName(), user.getAccountBalance()))
                .toList();
    }


}
