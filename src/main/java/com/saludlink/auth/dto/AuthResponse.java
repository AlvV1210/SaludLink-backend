package com.saludlink.auth.dto;

import com.saludlink.auth.model.UserRole;

public record AuthResponse(
        String token,
        String email,
        UserRole role,
        String firstName,
        String lastName) {}
