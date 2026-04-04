package com.danieldiego.trackMaintenance.crosscutting.config;

import com.danieldiego.trackMaintenance.application.Interface.jwt.JwtTokenPort;
import com.danieldiego.trackMaintenance.application.Interface.manutencao.ManutencaoRepositoryPort;
import com.danieldiego.trackMaintenance.application.Interface.security.PasswordEncoderPort;
import com.danieldiego.trackMaintenance.application.Interface.user.UserRepositoryPort;
import com.danieldiego.trackMaintenance.application.Interface.veiculo.VeiculoRepositoryPort;
import com.danieldiego.trackMaintenance.application.service.manutencao.ManutencaoService;
import com.danieldiego.trackMaintenance.application.service.manutencao.ManutencaoServiceImpl;
import com.danieldiego.trackMaintenance.application.service.user.UserService;
import com.danieldiego.trackMaintenance.application.service.user.UserServiceImpl;
import com.danieldiego.trackMaintenance.application.service.veiculo.VeiculoService;
import com.danieldiego.trackMaintenance.application.service.veiculo.VeiculoServiceImpl;
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

    @Bean
    public VeiculoService veiculoService(VeiculoRepositoryPort veiculoRepository) {
        return new VeiculoServiceImpl(veiculoRepository);
    }

    @Bean
    public ManutencaoService manutencaoService(ManutencaoRepositoryPort manutencaoRepository,
                                               VeiculoRepositoryPort veiculoRepository) {
        return new ManutencaoServiceImpl(manutencaoRepository, veiculoRepository);
    }
}