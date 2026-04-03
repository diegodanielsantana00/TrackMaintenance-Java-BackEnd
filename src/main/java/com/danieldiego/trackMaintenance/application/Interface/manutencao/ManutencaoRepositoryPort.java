package com.danieldiego.trackMaintenance.application.Interface.manutencao;

import com.danieldiego.trackMaintenance.domain.model.Manutencao;
import com.danieldiego.trackMaintenance.domain.model.StatusManutencao;

import java.util.List;
import java.util.Optional;

public interface ManutencaoRepositoryPort {

    Manutencao save(Manutencao manutencao);

    Optional<Manutencao> findById(Long id);

    List<Manutencao> findAll();

    List<Manutencao> findByVeiculoId(Long veiculoId);

    List<Manutencao> findByStatus(StatusManutencao status);

    void deleteById(Long id);
}
