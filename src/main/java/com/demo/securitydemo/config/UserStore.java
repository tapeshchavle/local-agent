package com.demo.securitydemo.config;

import com.demo.securitydemo.model.AppUser;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class UserStore {

    private final Map<String, AppUser> users = new ConcurrentHashMap<>();

    public UserStore(PasswordEncoder passwordEncoder) {
        // Demo users. Passwords are encoded with BCrypt before being stored in the Map.
        users.put("student",
                new AppUser("student", passwordEncoder.encode("student123"), "STUDENT"));

        users.put("admin",
                new AppUser("admin", passwordEncoder.encode("admin123"), "ADMIN"));
    }

    public AppUser findByUsername(String username) {
        return users.get(username);
    }

    public Map<String, AppUser> getAllUsers() {
        return Map.copyOf(users);
    }

    public void deleteUser(String username) {
        users.remove(username);
    }
}
