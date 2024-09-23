package com.example.bookingDemo.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum RoleEnum {
    USER("USER", "User"),
    ADMIN("ADMIN", "Admin");

    private String code;
    private String name;

}
