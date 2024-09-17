package com.example.bookingDemo.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum StatusEnum {
    SUCCESS("SUCCESS", 100),
    FAIL("FAIL", 400);

    private final String code;
    private final int message;

}
