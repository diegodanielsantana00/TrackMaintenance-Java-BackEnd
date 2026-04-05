package com.danieldiego.trackMaintenance.application.Interface.manutencao;

import com.danieldiego.trackMaintenance.domain.model.Manutencao;
import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ManutencaoRepositoryPort {

    Manutencao save(Manutencao manutencao);

    Optional<Manutencao> findById(Long id);

    List<Manutencao> findAll();

    Page<Manutencao> findAll(Pageable pageable);

    List<Manutencao> findByVeiculoId(Long veiculoId);

    List<Manutencao> findByStatus(StatusManutencao status);

    boolean existsActiveByVeiculoId(Long veiculoId);

    void deleteById(Long id);
}
