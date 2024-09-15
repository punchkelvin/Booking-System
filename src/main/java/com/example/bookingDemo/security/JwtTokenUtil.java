package com.example.bookingDemo.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;

@Component
public class JwtTokenUtil {
    @Value("${jwt.secret.key}")
    private String jwtSecretKey;

    public String getJwtSecretKey(){
        return jwtSecretKey;
    }

    //Generate Token for user
    public String generateToken(String username){
        HashMap<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    private String createToken(HashMap<String, Object> claims, String subject){
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)
                .compact();
    }

}
