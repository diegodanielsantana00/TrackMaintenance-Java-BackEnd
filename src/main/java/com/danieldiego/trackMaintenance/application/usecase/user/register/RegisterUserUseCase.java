package com.danieldiego.trackMaintenance.application.usecase.user.register;

import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;

public interface RegisterUserUseCase {

    RegisterUserOutput execute(RegisterUserCommand command);
}
