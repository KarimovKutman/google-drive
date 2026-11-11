package dev.kutman.google_drivve.auth.dto;

import dev.kutman.google_drivve.dto.UserResponse;

public record AuthResponse(
        String tokenType,
        String accessToken,
        String refreshToken,
        long expiresInSeconds,
        UserResponse user
) {
}
