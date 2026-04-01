package com.danieldiego.trackMaintenance.application.dto.user.register;

import java.util.UUID;

public record RegisterUserOutput(UUID userId, String name, String email, String token) {
}
