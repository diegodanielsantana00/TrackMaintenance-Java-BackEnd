package com.danieldiego.trackMaintenance.application.usecase.user.login;

import com.danieldiego.trackMaintenance.application.port.jwt.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.port.security.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.port.user.UserRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.user.login.LoginUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;
import com.danieldiego.trackMaintenance.domain.exception.InvalidCredentialsException;
import com.danieldiego.trackMaintenance.domain.model.User;

public class LoginUserUseCaseImpl implements LoginUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public LoginUserUseCaseImpl(UserRepositoryPort userRepository,
                                 PasswordEncoderPort passwordEncoder,
                                 JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    public RegisterUserOutput execute(LoginUserCommand command) {
        User user = userRepository.findByEmail(command.email())
                .orElseThrow(InvalidCredentialsException::new);

        if (!passwordEncoder.matches(command.password(), user.getPassword())) {
            throw new InvalidCredentialsException();
        }

        String token = jwtTokenPort.generateToken(user);

        return new RegisterUserOutput(
                user.getId(),
                user.getName(),
                user.getEmail(),
                token
        );
    }
}
