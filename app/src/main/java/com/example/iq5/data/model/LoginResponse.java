package com.example.iq5.data.model;

public class LoginResponse {
    private String token; // JWT token
    private UserDto user;

    public LoginResponse(String token, UserDto user) {
        this.token = token;
        this.user = user;
    }

    // Getters và Setters (bỏ qua cho ngắn gọn)
    // ...
}