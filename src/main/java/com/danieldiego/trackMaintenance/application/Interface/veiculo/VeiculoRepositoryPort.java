package com.danieldiego.trackMaintenance.application.Interface.veiculo;

import com.danieldiego.trackMaintenance.domain.model.Veiculo;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VeiculoRepositoryPort {

    Veiculo save(Veiculo veiculo);

    Optional<Veiculo> findById(Long id);

    Optional<Veiculo> findByPlaca(String placa);

    List<Veiculo> findAll();

    Page<Veiculo> findAll(Pageable pageable);

    boolean existsByPlaca(String placa);

    void deleteById(Long id);
}
