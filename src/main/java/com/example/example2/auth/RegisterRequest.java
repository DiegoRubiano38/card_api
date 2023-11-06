package com.example.example2.auth;

public record RegisterRequest(
        String first_name,
        String last_name,
        String email,
        String password
) {
}
