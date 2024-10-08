package com.example.bookingDemo.controller;

import com.example.bookingDemo.dto.token.ExtractTokenRequest;
import com.example.bookingDemo.dto.authentication.AuthenticationRequest;
import com.example.bookingDemo.model.RefreshToken;
import com.example.bookingDemo.security.JwtTokenUtil;
import com.example.bookingDemo.security.KeyGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.crypto.SecretKey;

@RestController
@RequestMapping("/token")
public class TokenController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;
    @Autowired
    KeyGenerator keyGenerator;
    @GetMapping("/generatebBase64Secret")
    public String generatebBase64Secret(){
        return keyGenerator.getBase64SecretKey();
    }

    @GetMapping("/getSecret")
    public SecretKey getSecret(){
        return jwtTokenUtil.getJwtSigningKey();
    }

    @PostMapping("/generateAccessToken")
    public String generateAccessToken(@RequestBody AuthenticationRequest authenticationRequest){
        return jwtTokenUtil.generateAccessToken(authenticationRequest.getUsername());
    }

    @PostMapping("/generateRefreshToken")
    public RefreshToken generateRefreshToken(@RequestBody AuthenticationRequest authenticationRequest){
        return jwtTokenUtil.generateRefreshToken(authenticationRequest.getUsername());
    }

    @PostMapping("/extractUsername")
    public String extractUsername(@RequestBody ExtractTokenRequest extractTokenRequest){
        return jwtTokenUtil.extractUsername(extractTokenRequest.getToken());
    }

    @PostMapping("/validateToken")
    public boolean validateToken(@RequestBody ExtractTokenRequest extractTokenRequest){
        return jwtTokenUtil.validateToken(extractTokenRequest.getToken(), extractTokenRequest.getUsername());
    }

    /**
     * Token is stored in HTTP only cookie, so the client doesn't have to handle it directly
     * **/
    @PostMapping("/getNewAccessTokenWithRefresh")
    public String getNewAccessTokenWithRefresh(@CookieValue("refreshToken") String refreshToken){
        return jwtTokenUtil.getNewAccessTokenWithRefresh(refreshToken);
    }
}
