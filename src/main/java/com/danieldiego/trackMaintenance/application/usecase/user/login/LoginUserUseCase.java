package com.danieldiego.trackMaintenance.application.usecase.user.login;

import com.danieldiego.trackMaintenance.application.dto.user.login.LoginUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;

public interface LoginUserUseCase {

    RegisterUserOutput execute(LoginUserCommand command);
}
