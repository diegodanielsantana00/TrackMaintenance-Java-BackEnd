package com.danieldiego.trackMaintenance.application.Interface.jwt;

import com.danieldiego.trackMaintenance.domain.model.User;

import java.util.UUID;

public interface JwtTokenPort {

    String generateToken(User user);

    UUID extractUserId(String token);

    boolean isTokenValid(String token);
}
