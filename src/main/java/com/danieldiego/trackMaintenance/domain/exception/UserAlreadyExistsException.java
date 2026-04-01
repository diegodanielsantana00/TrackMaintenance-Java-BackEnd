package com.danieldiego.trackMaintenance.domain.exception;

public class UserAlreadyExistsException extends BusinessException {

    public UserAlreadyExistsException(String email) {
        super("User with email '" + email + "' already exists");
    }
}
