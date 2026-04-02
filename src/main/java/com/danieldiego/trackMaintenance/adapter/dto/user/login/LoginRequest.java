package com.danieldiego.trackMaintenance.adapter.dto.user.login;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Login request payload")
public record LoginRequest(

        @Schema(description = "User's email address", example = "contato@danieldiegosantana.me")
        @NotBlank(message = "Email is required")
        @Email(message = "Invalid email format")
        String email,

        @Schema(description = "User's password", example = "P@ssw0rd123")
        @NotBlank(message = "Password is required")
        String password
) {
}
