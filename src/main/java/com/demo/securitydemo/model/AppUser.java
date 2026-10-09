package com.demo.securitydemo.model;

public record AppUser(
        String username,
        String password,
        String role
) {
}
