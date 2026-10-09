package com.demo.securitydemo.dto;

public record LoginResponse(
        String message,
        String username,
        String role,
        String token
) {
}
