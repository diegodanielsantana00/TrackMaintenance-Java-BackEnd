package com.danieldiego.trackMaintenance.infrastructure.persistence.repository;

import com.danieldiego.trackMaintenance.infrastructure.persistence.entity.VeiculoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VeiculoJpaRepository extends JpaRepository<VeiculoJpaEntity, Long> {

    Optional<VeiculoJpaEntity> findByPlaca(String placa);

    boolean existsByPlaca(String placa);
}
