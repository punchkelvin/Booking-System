package com.example.bookingDemo.controller;

import com.example.bookingDemo.security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    JwtTokenUtil jwtTokenUtil;

    @GetMapping("/secretKey")
    public String printSecretKey(){
        return jwtTokenUtil.getJwtSecretKey();
    }
}
