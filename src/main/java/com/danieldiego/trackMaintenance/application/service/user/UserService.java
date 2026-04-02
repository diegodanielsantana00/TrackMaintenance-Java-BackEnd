package com.danieldiego.trackMaintenance.application.service.user;

import com.danieldiego.trackMaintenance.application.dto.user.login.LoginUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;
import com.danieldiego.trackMaintenance.application.dto.user.UserProfileOutput;

import java.util.UUID;

public interface UserService {

    RegisterUserOutput register(RegisterUserCommand command);

    RegisterUserOutput login(LoginUserCommand command);

    UserProfileOutput getMe(UUID userId);
}
