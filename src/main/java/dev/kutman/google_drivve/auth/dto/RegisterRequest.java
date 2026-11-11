package dev.kutman.google_drivve.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;

public record RegisterRequest(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name must be at most 100 characters")
        String name,

        @NotBlank(message = "Surname is required")
        @Size(max = 100, message = "Surname must be at most 100 characters")
        String surname,

        @NotBlank(message = "Username is required")
        @Size(min = 3, max = 50, message = "Username must contain 3 to 50 characters")
        @Pattern(regexp = "^[a-zA-Z0-9._-]+$", message = "Username may contain only letters, numbers, dots, underscores, and hyphens")
        String username,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be valid")
        @Size(max = 100, message = "Email must be at most 100 characters")
        String email,

        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 72, message = "Password must contain 8 to 72 characters")
        String password
) {
    // BCrypt accepts at most 72 UTF-8 bytes, not 72 Unicode characters.
    @jakarta.validation.constraints.AssertTrue(message = "Password must be at most 72 UTF-8 bytes")
    public boolean isPasswordByteLengthValid() {
        return password == null || password.getBytes(java.nio.charset.StandardCharsets.UTF_8).length <= 72;
    }
}
