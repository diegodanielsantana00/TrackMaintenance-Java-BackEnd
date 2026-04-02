package com.danieldiego.trackMaintenance.adapter.dto.user.register;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Registration request payload")
public record RegisterRequest(

        @Schema(description = "User's full name", example = "John Doe")
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 150, message = "Name must be between 3 and 150 characters")
        String name,

        @Schema(description = "User's email address", example = "contato@danieldiegosantana.me")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "User's password (min 8 characters)", example = "P@ssw0rd123")
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 100, message = "Password must be between 8 and 100 characters")
        String password
) {
}
