package com.example.simpleBankingSystemChallenge.service.serviceImp;

import com.example.simpleBankingSystemChallenge.dto.UserDto;
import com.example.simpleBankingSystemChallenge.exception.OurException;
import com.example.simpleBankingSystemChallenge.model.User;
import com.example.simpleBankingSystemChallenge.repository.UserRepository;
import com.example.simpleBankingSystemChallenge.service.JwtService;
import com.example.simpleBankingSystemChallenge.service.UserService;
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
public class UserServiceImp implements UserService {

    private final UserRepository repo;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    //  Constructor
    @Autowired
    public UserServiceImp(UserRepository repo, AuthenticationManager authenticationManager,JwtService service) {
        this.repo = repo;
        this.authenticationManager =authenticationManager;
        this.jwtService =service;
    }


    //  Create new User Account
    @Override
    public UserDto createAccount(User user) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
        user.setPassword(encoder.encode(user.getPassword()));

       User saveUuser = repo.save(user);
       return Utils.toDto(saveUuser);
    }

    @Override
    public String verify(User user) {
        Authentication authentication =authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(user);
        }
        return "failed";
    }



    //    Withdraw from user account
    @Override
    public UserDto withdraw(long userId, BigDecimal amount) {
        try {

            User savedUser = repo.findById(userId).orElseThrow(() -> new OurException("User Not Found"));
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

        User savedUser;
        try {
            savedUser = repo.findById(userId).orElseThrow(() -> new OurException("User Not Found!!!"));
        } catch (OurException e) {
            throw new RuntimeException(e);
        }
        return savedUser.getAccountBalance();
    }


    @Override
    public String transfer(long userId, String recipientAccount, BigDecimal amount) {

        try {
            User recipient = repo.findByAccountNumber(recipientAccount).orElseThrow(() -> new OurException("Recipient Account Not Found!!!"));

            BigDecimal recipientAccountBalance = recipient.getAccountBalance();
            recipientAccountBalance = recipientAccountBalance.add(amount);
            recipient.setAccountBalance(recipientAccountBalance);

            UserDto dto = withdraw(userId, amount);

            return dto+" transferred "+amount+" to " +recipientAccount;

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public UserDto updateUser(long userId,User user) {
        User savedUser = null;
        try {
            savedUser = repo.findById(userId).orElseThrow(()-> new OurException("User Not Found!!"));
            repo.save(user);
            return  Utils.toDto(savedUser);

        } catch (OurException e) {
            throw new RuntimeException(e);
        }
    }


    public List<UserDto> getAllUser() {
        return repo.findAll()
                .stream()
                .map(user->
                   new UserDto(user.getUserId(), user.getLastName(), user.getFirstName(), user.getAccountBalance()))
                .toList();
    }
}
