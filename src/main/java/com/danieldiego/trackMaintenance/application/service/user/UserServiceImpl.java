package com.danieldiego.trackMaintenance.application.service.user;

import com.danieldiego.trackMaintenance.application.Interface.jwt.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.Interface.security.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.Interface.user.UserRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.user.login.LoginUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserCommand;
import com.danieldiego.trackMaintenance.application.dto.user.register.RegisterUserOutput;
import com.danieldiego.trackMaintenance.application.dto.user.UserProfileOutput;
import com.danieldiego.trackMaintenance.domain.exception.InvalidCredentialsException;
import com.danieldiego.trackMaintenance.domain.exception.UserAlreadyExistsException;
import com.danieldiego.trackMaintenance.domain.exception.UserNotFoundException;
import com.danieldiego.trackMaintenance.domain.model.User;

import java.util.UUID;

public class UserServiceImpl implements UserService {

    private final UserRepositoryPort userRepository;
    private final PasswordEncoderPort passwordEncoder;
    private final JwtTokenPort jwtTokenPort;

    public UserServiceImpl(UserRepositoryPort userRepository,
                           PasswordEncoderPort passwordEncoder,
                           JwtTokenPort jwtTokenPort) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenPort = jwtTokenPort;
    }

    @Override
    public RegisterUserOutput register(RegisterUserCommand command) {
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

    @Override
    public RegisterUserOutput login(LoginUserCommand command) {
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

    @Override
    public UserProfileOutput getMe(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));

        return new UserProfileOutput(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getCreatedAt()
        );
    }
}
