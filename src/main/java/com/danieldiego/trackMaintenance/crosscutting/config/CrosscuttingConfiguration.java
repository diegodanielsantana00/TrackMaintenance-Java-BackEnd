package com.danieldiego.trackMaintenance.crosscutting.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Arrays;

@Configuration
@Import({
    BeanConfiguration.class,
    com.danieldiego.trackMaintenance.crosscutting.security.SecurityConfig.class,
    com.danieldiego.trackMaintenance.crosscutting.openapi.OpenApiConfig.class,
    com.danieldiego.trackMaintenance.crosscutting.exception.GlobalExceptionHandler.class
})
public class CrosscuttingConfiguration {

    private final Environment environment;

    public CrosscuttingConfiguration(Environment environment) {
        this.environment = environment;
    }

    @Bean
    @Profile("!test")
    public CommandLineRunner systemInitializer() {
        return args -> {
            
            String[] activeProfiles = environment.getActiveProfiles();
            if (activeProfiles.length > 0) {
                System.out.println("Active profiles: " + Arrays.toString(activeProfiles));
            } else {
                System.out.println("Active profiles: [default]");
            }
            
        };
    }

    @Bean
    public SystemConfigValidator systemConfigValidator() {
        return () -> {
            String jwtSecret = environment.getProperty("jwt.secret");
            if (jwtSecret == null || jwtSecret.trim().isEmpty()) {
                throw new IllegalStateException("JWT secret must be configured");
            }
            
            String dbUrl = environment.getProperty("spring.datasource.url");
            if (dbUrl == null || dbUrl.trim().isEmpty()) {
                throw new IllegalStateException("Database URL must be configured");
            }
        };
    }
    
    @FunctionalInterface
    public interface SystemConfigValidator {
        void validateConfiguration();
    }
}