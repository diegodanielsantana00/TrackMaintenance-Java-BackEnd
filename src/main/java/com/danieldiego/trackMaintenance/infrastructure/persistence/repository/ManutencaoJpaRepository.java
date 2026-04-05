package com.danieldiego.trackMaintenance.infrastructure.persistence.repository;

import com.danieldiego.trackMaintenance.domain.model.StatusManutencao;
import com.danieldiego.trackMaintenance.infrastructure.persistence.entity.ManutencaoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManutencaoJpaRepository extends JpaRepository<ManutencaoJpaEntity, Long> {

    List<ManutencaoJpaEntity> findByVeiculoId(Long veiculoId);

    List<ManutencaoJpaEntity> findByStatus(StatusManutencao status);

    boolean existsByVeiculoIdAndStatusIn(Long veiculoId, List<StatusManutencao> statuses);
}
