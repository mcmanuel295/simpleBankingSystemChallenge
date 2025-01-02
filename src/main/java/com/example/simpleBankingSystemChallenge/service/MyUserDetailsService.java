package com.example.simpleBankingSystemChallenge.service;

import com.example.simpleBankingSystemChallenge.model.MyUserDetails;
import com.example.simpleBankingSystemChallenge.exception.OurException;
import com.example.simpleBankingSystemChallenge.model.User;
import com.example.simpleBankingSystemChallenge.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {
    UserRepository repo;

    public MyUserDetailsService(UserRepository repo) {
        this.repo = repo;
    }

    @Override
    public UserDetails loadUserByUsername(String username){
        User user =repo.findByUsername(username);

        if (user == null) {
            try {
                throw new OurException("User Not Found!!!");
            } catch (OurException e) {
                throw new RuntimeException(e);
            }
        }
        return new MyUserDetails(user);
    }
}
