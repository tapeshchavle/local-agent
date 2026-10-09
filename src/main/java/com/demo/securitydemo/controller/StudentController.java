package com.demo.securitydemo.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    @GetMapping("/profile")
    public Map<String, Object> profile(Authentication authentication) {

        return Map.of(
                "message", "Student API accessed successfully",
                "username", authentication.getName(),
                "role", authentication.getAuthorities()
                        .iterator().next().getAuthority()
        );
    }
}
