package com.danieldiego.trackMaintenance.application.service.veiculo;

import com.danieldiego.trackMaintenance.application.dto.veiculo.CreateVeiculoCommand;
import com.danieldiego.trackMaintenance.application.dto.veiculo.VeiculoOutput;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface VeiculoService {

    VeiculoOutput createVeiculo(CreateVeiculoCommand command);

    VeiculoOutput getVeiculoById(Long id);

    List<VeiculoOutput> getAllVeiculos();

    Page<VeiculoOutput> getAllVeiculos(Pageable pageable);

    VeiculoOutput updateVeiculo(Long id, CreateVeiculoCommand command);

    void deleteVeiculo(Long id);
}
