package com.danieldiego.trackMaintenance.application.service.veiculo;

import com.danieldiego.trackMaintenance.application.dto.veiculo.CreateVeiculoCommand;
import com.danieldiego.trackMaintenance.application.dto.veiculo.VeiculoOutput;

import java.util.List;

public interface VeiculoService {

    VeiculoOutput createVeiculo(CreateVeiculoCommand command);

    VeiculoOutput getVeiculoById(Long id);

    List<VeiculoOutput> getAllVeiculos();

    VeiculoOutput updateVeiculo(Long id, CreateVeiculoCommand command);

    void deleteVeiculo(Long id);
}
