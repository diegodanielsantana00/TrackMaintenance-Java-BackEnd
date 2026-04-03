package com.danieldiego.trackMaintenance.infrastructure.persistence.repository;

import com.danieldiego.trackMaintenance.infrastructure.persistence.entity.ViagemJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ViagemJpaRepository extends JpaRepository<ViagemJpaEntity, Long> {

    List<ViagemJpaEntity> findByVeiculoId(Long veiculoId);
}
