package com.example.userservice.dto;

import com.example.userservice.model.User;

public record AuthResponse(
    String token,
    String type,
    long expiresIn,
    UserInfo user
) {
    public AuthResponse(String token, long expiresIn, User user) {
        this(
            token,
            "Bearer",
            expiresIn,
            new UserInfo(user.getId(), user.getName(), user.getEmail(), user.getRole().name())
        );
    }

    public record UserInfo(Long id, String name, String email, String role) {}
}
