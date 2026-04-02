package com.danieldiego.trackMaintenance.application.dto.user;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileOutput(
        UUID id,
        String name,
        String email,
        LocalDateTime createdAt
) {}
