package com.danieldiego.trackMaintenance.domain.exception;

import java.util.UUID;

public class UserNotFoundException extends BusinessException {

    public UserNotFoundException(UUID id) {
        super("User with id '" + id + "' not found");
    }
}
