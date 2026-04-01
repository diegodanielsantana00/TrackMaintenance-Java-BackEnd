package com.danieldiego.trackMaintenance.application.usecase;

import com.danieldiego.trackMaintenance.domain.exception.UserAlreadyExistsException;
import com.danieldiego.trackMaintenance.domain.model.User;
import com.danieldiego.trackMaintenance.application.port.in.RegisterUserCommand;
import com.danieldiego.trackMaintenance.application.port.in.RegisterUserOutput;
import com.danieldiego.trackMaintenance.application.port.out.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.port.out.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class RegisterUserUseCaseImplTest {

    private RegisterUserUseCaseImpl useCase;
    private UserRepositoryPort userRepository;
    private PasswordEncoderPort passwordEncoder;
    private JwtTokenPort jwtTokenPort;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepositoryPort.class);
        passwordEncoder = mock(PasswordEncoderPort.class);
        jwtTokenPort = mock(JwtTokenPort.class);
        useCase = new RegisterUserUseCaseImpl(userRepository, passwordEncoder, jwtTokenPort);
    }

    @Test
    @DisplayName("Should register user successfully and return JWT")
    void shouldRegisterUserSuccessfully() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("P@ssw0rd123")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(jwtTokenPort.generateToken(any(User.class))).thenReturn("jwt-token");

        RegisterUserOutput output = useCase.execute(
                new RegisterUserCommand("John Doe", "john@example.com", "P@ssw0rd123"));

        assertThat(output.name()).isEqualTo("John Doe");
        assertThat(output.email()).isEqualTo("john@example.com");
        assertThat(output.token()).isEqualTo("jwt-token");
        assertThat(output.userId()).isNotNull();
        verify(userRepository).save(any(User.class));
        verify(passwordEncoder).encode("P@ssw0rd123");
    }

    @Test
    @DisplayName("Should throw UserAlreadyExistsException when email is taken")
    void shouldThrowWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(true);

        assertThatThrownBy(() -> useCase.execute(
                new RegisterUserCommand("John Doe", "john@example.com", "P@ssw0rd123")))
                .isInstanceOf(UserAlreadyExistsException.class)
                .hasMessageContaining("john@example.com");

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("Should encode password before saving")
    void shouldEncodePasswordBeforeSaving() {
        when(userRepository.existsByEmail("john@example.com")).thenReturn(false);
        when(passwordEncoder.encode("P@ssw0rd123")).thenReturn("$2a$10$encodedHash");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User saved = invocation.getArgument(0);
            assertThat(saved.getPassword()).isEqualTo("$2a$10$encodedHash");
            return saved;
        });
        when(jwtTokenPort.generateToken(any(User.class))).thenReturn("token");

        useCase.execute(new RegisterUserCommand("John Doe", "john@example.com", "P@ssw0rd123"));

        verify(passwordEncoder).encode("P@ssw0rd123");
    }
}
