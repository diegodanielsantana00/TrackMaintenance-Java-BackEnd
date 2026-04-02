package com.danieldiego.trackMaintenance.infrastructure.config;

import com.danieldiego.trackMaintenance.application.Interface.jwt.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.Interface.security.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.Interface.user.UserRepositoryPort;
import com.danieldiego.trackMaintenance.application.service.user.UserService;
import com.danieldiego.trackMaintenance.application.service.user.UserServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(JwtProperties.class)
public class BeanConfiguration {

    @Bean
    public UserService userService(UserRepositoryPort userRepository,
                                   PasswordEncoderPort passwordEncoder,
                                   JwtTokenPort jwtTokenPort) {
        return new UserServiceImpl(userRepository, passwordEncoder, jwtTokenPort);
    }
}
