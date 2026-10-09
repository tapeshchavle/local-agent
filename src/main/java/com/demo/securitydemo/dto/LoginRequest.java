package com.demo.securitydemo.dto;

public record LoginRequest(
        String username,
        String password
) {
}
