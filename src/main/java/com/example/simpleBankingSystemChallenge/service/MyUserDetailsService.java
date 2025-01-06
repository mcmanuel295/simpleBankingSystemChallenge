package com.example.simpleBankingSystemChallenge.service;

import com.example.simpleBankingSystemChallenge.model.Account;
import com.example.simpleBankingSystemChallenge.model.MyUserDetails;
import com.example.simpleBankingSystemChallenge.exception.OurException;
import com.example.simpleBankingSystemChallenge.repository.AccountRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    AccountRepository repo;

    public MyUserDetailsService(AccountRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        Account account =repo.findByUsername(username);

        if (account == null) {
            try {
                throw new OurException("User Not Found!!!");
            } catch (OurException e) {
                throw new RuntimeException(e);
            }
        }
        return new MyUserDetails(account);
    }
}
