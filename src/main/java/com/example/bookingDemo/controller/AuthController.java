package com.example.bookingDemo.controller;

import com.example.bookingDemo.dto.authentication.AuthenticationRequest;
import com.example.bookingDemo.dto.authentication.AuthenticationResponse;
import com.example.bookingDemo.dto.authentication.RegisterRequest;
import com.example.bookingDemo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest registerRequest){
        return ResponseEntity.ok(userService.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest){
        //Authenticate User
        AuthenticationResponse authenticationResponse = userService.authenticate(authenticationRequest);

        //Set refresh token in HttpOnly, secure cookie
        ResponseCookie responseCookie = ResponseCookie.from("refreshToken", authenticationResponse.getRefreshToken().getRefreshToken())
                .httpOnly(true) // prevent javascript access, protect against xss
                .secure(true) // only sent through https
                .path("/")
                .maxAge(7 * 24 * 60 * 60) // 7 days
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(authenticationResponse);
    }

    @GetMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue("refreshToken") String refreshToken){
        return ResponseEntity.ok(userService.logout(refreshToken));
    }
}
