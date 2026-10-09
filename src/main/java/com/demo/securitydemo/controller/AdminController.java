package com.demo.securitydemo.controller;

import com.demo.securitydemo.config.UserStore;
import com.demo.securitydemo.model.AppUser;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    private final UserStore userStore;

    public AdminController(UserStore userStore) {
        this.userStore = userStore;
    }

    @GetMapping("/users")
    public List<Map<String, String>> getUsers() {
        return userStore.getAllUsers().values().stream()
                .map(user -> Map.of(
                        "username", user.username(),
                        "role", user.role()
                ))
                .toList();
    }

    @DeleteMapping("/users/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username) {

        AppUser user = userStore.findByUsername(username);

        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        userStore.deleteUser(username);

        return ResponseEntity.ok(
                Map.of(
                        "message", "User deleted successfully",
                        "username", username
                )
        );
    }
}
