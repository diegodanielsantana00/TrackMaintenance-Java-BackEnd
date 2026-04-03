package com.danieldiego.trackMaintenance.application.Interface.veiculo;

import com.danieldiego.trackMaintenance.domain.model.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepositoryPort {

    Veiculo save(Veiculo veiculo);

    Optional<Veiculo> findById(Long id);

    Optional<Veiculo> findByPlaca(String placa);

    List<Veiculo> findAll();

    boolean existsByPlaca(String placa);

    void deleteById(Long id);
}
