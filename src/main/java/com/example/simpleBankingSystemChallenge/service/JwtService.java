package com.example.simpleBankingSystemChallenge.service;

import com.example.simpleBankingSystemChallenge.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.function.Function;

@Service
public class JwtService {

    static String secretKeyString = "" ;

    public JwtService() {
        KeyGenerator keyGen = null;
        try {
            keyGen = KeyGenerator.getInstance("HmacSHA256");
            SecretKey secretKey = keyGen.generateKey();
            secretKeyString = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    public SecretKey getKey(){
        byte[] keyByte = Base64.getDecoder().decode(secretKeyString.getBytes());
        return Keys.hmacShaKeyFor(keyByte);
    }




    public boolean validate(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return username.equals(userDetails.getUsername())  && isTokenExpired(token);
    }


    private boolean isTokenExpired (String token){
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }



    public String extractUsername(String token){
        return extractClaim(token,Claims::getSubject);
    }



    private <T> T extractClaim(String token,Function<Claims,T> claimResolver){
        final Claims claims =  extractAllClaims(token);
        return claimResolver.apply(claims);
    }


    public Claims extractAllClaims(String token) {

        return Jwts
                .parser()
                .verifyWith(getKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }


    public String generateToken(User user) {
        Map<String, Object> claims = new HashMap<>();

        return Jwts.builder()
                .claims()
                .add(claims)
                .subject(user.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))
                .and()
                .signWith(getKey())
                .compact();
    }
}
