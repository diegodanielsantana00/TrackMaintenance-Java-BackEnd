package com.danieldiego.trackMaintenance.application.service.manutencao;

import com.danieldiego.trackMaintenance.application.dto.manutencao.CreateManutencaoCommand;
import com.danieldiego.trackMaintenance.application.dto.manutencao.ManutencaoOutput;
import com.danieldiego.trackMaintenance.application.dto.manutencao.UpdateManutencaoCommand;
import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ManutencaoService {

    ManutencaoOutput createManutencao(CreateManutencaoCommand command);

    ManutencaoOutput getManutencaoById(Long id);

    List<ManutencaoOutput> getAllManutencoes();

    Page<ManutencaoOutput> getAllManutencoes(Pageable pageable);

    List<ManutencaoOutput> getManutencoesByVeiculo(Long veiculoId);

    List<ManutencaoOutput> getManutencoesByStatus(StatusManutencao status);

    ManutencaoOutput updateManutencao(Long id, UpdateManutencaoCommand command);

    ManutencaoOutput updateStatus(Long id, StatusManutencao status);

    void deleteManutencao(Long id);
}
