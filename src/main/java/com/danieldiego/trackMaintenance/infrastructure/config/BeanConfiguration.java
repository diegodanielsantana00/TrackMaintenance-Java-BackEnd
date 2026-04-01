package com.danieldiego.trackMaintenance.infrastructure.config;

import com.danieldiego.trackMaintenance.application.port.jwt.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.port.security.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.port.user.UserRepositoryPort;
import com.danieldiego.trackMaintenance.application.usecase.user.login.LoginUserUseCase;
import com.danieldiego.trackMaintenance.application.usecase.user.login.LoginUserUseCaseImpl;
import com.danieldiego.trackMaintenance.application.usecase.user.register.RegisterUserUseCase;
import com.danieldiego.trackMaintenance.application.usecase.user.register.RegisterUserUseCaseImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class BeanConfiguration {

    @Bean
    public RegisterUserUseCase registerUserUseCase(UserRepositoryPort userRepository,
                                                    PasswordEncoderPort passwordEncoder,
                                                    JwtTokenPort jwtTokenPort) {
        return new RegisterUserUseCaseImpl(userRepository, passwordEncoder, jwtTokenPort);
    }

    @Bean
    public LoginUserUseCase loginUserUseCase(UserRepositoryPort userRepository,
                                              PasswordEncoderPort passwordEncoder,
                                              JwtTokenPort jwtTokenPort) {
        return new LoginUserUseCaseImpl(userRepository, passwordEncoder, jwtTokenPort);
    }
}
