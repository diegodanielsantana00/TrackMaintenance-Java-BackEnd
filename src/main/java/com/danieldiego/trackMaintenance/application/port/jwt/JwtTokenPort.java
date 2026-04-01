package com.danieldiego.trackMaintenance.application.port.jwt;

import com.danieldiego.trackMaintenance.domain.model.User;

public interface JwtTokenPort {

    String generateToken(User user);
}
