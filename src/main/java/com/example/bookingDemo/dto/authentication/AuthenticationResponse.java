package com.example.bookingDemo.dto.authentication;

import com.example.bookingDemo.dto.BaseResponse;
import com.example.bookingDemo.model.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
public class AuthenticationResponse extends BaseResponse {
    private String accessToken;
    private RefreshToken refreshToken;
}
