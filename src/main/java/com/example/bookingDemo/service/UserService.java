package com.example.bookingDemo.service;

import com.example.bookingDemo.dto.authentication.AuthenticationRequest;
import com.example.bookingDemo.dto.authentication.AuthenticationResponse;
import com.example.bookingDemo.dto.authentication.RegisterRequest;
import com.example.bookingDemo.enums.StatusEnum;
import com.example.bookingDemo.exceptions.UserAlreadyExistsException;
import com.example.bookingDemo.enums.RoleEnum;
import com.example.bookingDemo.model.User;
import com.example.bookingDemo.repository.UserRepository;
import com.example.bookingDemo.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    AuthenticationManager authenticationManager;

    public AuthenticationResponse register(RegisterRequest registerRequest){

        //Check if the username has already been taken
        if(userRepository.findByUsername(registerRequest.getUsername()).isPresent()){
            throw new UserAlreadyExistsException("Username already taken");
        }

        //Create new User
        User user = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .role(RoleEnum.USER)
                .build();

        userRepository.save(user);

        //Generate Token
        String jwtToken = jwtTokenUtil.generateToken(registerRequest.getUsername());

        AuthenticationResponse authenticationResponse =
                AuthenticationResponse.builder()
                        .status(StatusEnum.SUCCESS.getCode())
                        .dateTime(LocalDateTime.now())
                        .token(jwtToken)
                        .build();
        return authenticationResponse;
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest){
        //Authenticate the user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.getUsername(),
                        authenticationRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);


        //Generate Token
        String jwtToken = jwtTokenUtil.generateToken(authenticationRequest.getUsername());

        AuthenticationResponse authenticationResponse =
                AuthenticationResponse.builder()
                        .status(StatusEnum.SUCCESS.getCode())
                        .dateTime(LocalDateTime.now())
                        .token(jwtToken)
                        .build();

        return authenticationResponse;
    }
}
