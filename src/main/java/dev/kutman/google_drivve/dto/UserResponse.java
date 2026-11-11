package dev.kutman.google_drivve.dto;

import dev.kutman.google_drivve.entity.User;
import dev.kutman.google_drivve.entity.enums.Role;

import java.time.Instant;

public record UserResponse(
        Long id,
        String name,
        String surname,
        String username,
        String email,
        Role role,
        Instant createdAt
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getSurname(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getCreatedAt()
        );
    }
}
