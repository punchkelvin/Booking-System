package com.example.bookingDemo.dto.token;

import com.example.bookingDemo.dto.authentication.AuthenticationRequest;
import lombok.Data;

@Data
public class ExtractTokenRequest extends AuthenticationRequest {
    private String token;
}
