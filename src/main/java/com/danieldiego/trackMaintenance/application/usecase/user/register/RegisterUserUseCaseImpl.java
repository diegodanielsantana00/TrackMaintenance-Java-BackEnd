package com.danieldiego.trackMaintenance.application.usecase.user.register;

import com.danieldiego.trackMaintenance.application.port.jwt.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.port.security.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.port.user.UserRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;
import com.danieldiego.trackMaintenance.domain.exception.UserAlreadyExistsException;
import com.danieldiego.trackMaintenance.domain.model.User;

public class RegisterUserUseCaseImpl implements RegisterUserUseCase {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public RegisterUserUseCaseImpl(UserRepositoryPort userRepository,
                                    PasswordEncoderPort passwordEncoder,
                                    JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    public RegisterUserOutput execute(RegisterUserCommand command) {
        if (userRepository.existsByEmail(command.email())) {
            throw new UserAlreadyExistsException(command.email());
        }

        String encodedPassword = passwordEncoder.encode(command.password());
        User user = User.create(command.name(), command.email(), encodedPassword);
        User savedUser = userRepository.save(user);

        String token = jwtTokenPort.generateToken(savedUser);

        return new RegisterUserOutput(
                savedUser.getId(),
                savedUser.getName(),
                savedUser.getEmail(),
                token
        );
    }
}
