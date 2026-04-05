package com.danieldiego.trackMaintenance.application.service.manutencao;

import com.danieldiego.trackMaintenance.application.Interface.manutencao.ManutencaoRepositoryPort;
import com.danieldiego.trackMaintenance.application.Interface.veiculo.VeiculoRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.manutencao.CreateManutencaoCommand;
import com.danieldiego.trackMaintenance.application.dto.manutencao.ManutencaoOutput;
import com.danieldiego.trackMaintenance.application.dto.manutencao.UpdateManutencaoCommand;
import com.danieldiego.trackMaintenance.domain.exception.ManutencaoNotFoundException;
import com.danieldiego.trackMaintenance.domain.exception.VeiculoNotFoundException;
import com.danieldiego.trackMaintenance.domain.model.Manutencao;
import com.danieldiego.trackMaintenance.domain.model.StatusManutencao;
import com.danieldiego.trackMaintenance.domain.model.Veiculo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class ManutencaoServiceImpl implements ManutencaoService {

    private final ManutencaoRepositoryPort manutencaoRepository;
    private final VeiculoRepositoryPort veiculoRepository;

    public ManutencaoServiceImpl(ManutencaoRepositoryPort manutencaoRepository,
                                  VeiculoRepositoryPort veiculoRepository) {
        this.manutencaoRepository = manutencaoRepository;
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public ManutencaoOutput createManutencao(CreateManutencaoCommand command) {
        Veiculo veiculo = veiculoRepository.findById(command.veiculoId())
                .orElseThrow(() -> new VeiculoNotFoundException(command.veiculoId()));

        if (manutencaoRepository.existsActiveByVeiculoId(veiculo.getId())) {
            throw new IllegalStateException(
                    "Veículo já possui uma manutenção ativa (PENDENTE ou EM_REALIZACAO). Finalize-a antes de criar outra.");
        }

        Manutencao manutencao = Manutencao.create(
                veiculo.getId(),
                command.dataInicio(),
                command.dataFinalizacao(),
                command.tipoServico(),
                command.custoEstimado()
        );

        return toOutput(manutencaoRepository.save(manutencao), veiculo);
    }

    @Override
    public ManutencaoOutput getManutencaoById(Long id) {
        Manutencao manutencao = manutencaoRepository.findById(id)
                .orElseThrow(() -> new ManutencaoNotFoundException(id));
        return toOutputWithVeiculo(manutencao);
    }

    @Override
    public List<ManutencaoOutput> getAllManutencoes() {
        return manutencaoRepository.findAll().stream()
                .map(this::toOutputWithVeiculo)
                .toList();
    }

    @Override
    public Page<ManutencaoOutput> getAllManutencoes(Pageable pageable) {
        return manutencaoRepository.findAll(pageable).map(this::toOutputWithVeiculo);
    }

    @Override
    public List<ManutencaoOutput> getManutencoesByVeiculo(Long veiculoId) {
        veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new VeiculoNotFoundException(veiculoId));
        return manutencaoRepository.findByVeiculoId(veiculoId).stream()
                .map(this::toOutputWithVeiculo)
                .toList();
    }

    @Override
    public List<ManutencaoOutput> getManutencoesByStatus(StatusManutencao status) {
        return manutencaoRepository.findByStatus(status).stream()
                .map(this::toOutputWithVeiculo)
                .toList();
    }

    @Override
    public ManutencaoOutput updateManutencao(Long id, UpdateManutencaoCommand command) {
        Manutencao existing = manutencaoRepository.findById(id)
                .orElseThrow(() -> new ManutencaoNotFoundException(id));

        existing.atualizar(
                command.dataInicio(),
                command.dataFinalizacao(),
                command.tipoServico(),
                command.custoEstimado(),
                command.status()
        );

        return toOutputWithVeiculo(manutencaoRepository.save(existing));
    }

    @Override
    public ManutencaoOutput updateStatus(Long id, StatusManutencao status) {
        Manutencao existing = manutencaoRepository.findById(id)
                .orElseThrow(() -> new ManutencaoNotFoundException(id));

        existing.atualizarStatus(status);

        return toOutputWithVeiculo(manutencaoRepository.save(existing));
    }

    @Override
    public void deleteManutencao(Long id) {
        if (manutencaoRepository.findById(id).isEmpty()) {
            throw new ManutencaoNotFoundException(id);
        }
        manutencaoRepository.deleteById(id);
    }

    private ManutencaoOutput toOutputWithVeiculo(Manutencao manutencao) {
        Veiculo veiculo = veiculoRepository.findById(manutencao.getVeiculoId())
                .orElseThrow(() -> new VeiculoNotFoundException(manutencao.getVeiculoId()));
        return toOutput(manutencao, veiculo);
    }

    private ManutencaoOutput toOutput(Manutencao m, Veiculo v) {
        return new ManutencaoOutput(
                m.getId(),
                v.getId(),
                v.getPlaca(),
                v.getModelo(),
                m.getDataInicio(),
                m.getDataFinalizacao(),
                m.getTipoServico(),
                m.getCustoEstimado(),
                m.getStatus()
        );
    }
}
