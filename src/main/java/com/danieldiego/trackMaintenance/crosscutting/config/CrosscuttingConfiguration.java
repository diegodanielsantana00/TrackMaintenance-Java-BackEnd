package com.danieldiego.trackMaintenance.crosscutting.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;

import java.util.Arrays;

/**
 * Configuração principal da camada Crosscutting - Responsável pelo startup e configuração do sistema
 * 
 * Esta configuração centraliza todas as preocupações transversais do sistema:
 * - Configuração de beans de aplicação
 * - Configurações de segurança
 * - Configurações de documentação (OpenAPI)
 * - Tratamento global de exceções
 * - Inicialização do sistema
 */
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

    /**
     * Bean de inicialização do sistema que executa após o startup Spring
     * Responsável por:
     * - Logging de informações de startup
     * - Validações de configuração
     * - Setup inicial do sistema
     */
    @Bean
    @Profile("!test")
    public CommandLineRunner systemInitializer() {
        return args -> {
            System.out.println("===================================================");
            System.out.println("        Track Maintenance System Started");
            System.out.println("===================================================");
            
            String[] activeProfiles = environment.getActiveProfiles();
            if (activeProfiles.length > 0) {
                System.out.println("Active profiles: " + Arrays.toString(activeProfiles));
            } else {
                System.out.println("Active profiles: [default]");
            }
            
            String serverPort = environment.getProperty("server.port", "8080");
            String contextPath = environment.getProperty("server.servlet.context-path", "");
            
            System.out.println("Server running on: http://localhost:" + serverPort + contextPath);
            System.out.println("API Documentation: http://localhost:" + serverPort + contextPath + "/swagger-ui.html");
            System.out.println("===================================================");
        };
    }

    /**
     * Bean de configuração de propriedades do sistema
     * Responsável por verificar configurações críticas no startup
     */
    @Bean
    public SystemConfigValidator systemConfigValidator() {
        return () -> {
            // Validação básica de configurações críticas
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
    
    /**
     * Interface funcional para validação de configuração do sistema
     */
    @FunctionalInterface
    public interface SystemConfigValidator {
        void validateConfiguration();
    }
}