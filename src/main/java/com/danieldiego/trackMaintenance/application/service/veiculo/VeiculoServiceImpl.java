package com.danieldiego.trackMaintenance.application.service.veiculo;

import com.danieldiego.trackMaintenance.application.Interface.veiculo.VeiculoRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.veiculo.CreateVeiculoCommand;
import com.danieldiego.trackMaintenance.application.dto.veiculo.VeiculoOutput;
import com.danieldiego.trackMaintenance.domain.exception.VeiculoAlreadyExistsException;
import com.danieldiego.trackMaintenance.domain.exception.VeiculoNotFoundException;
import com.danieldiego.trackMaintenance.domain.model.Veiculo;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class VeiculoServiceImpl implements VeiculoService {

    private final VeiculoRepositoryPort veiculoRepository;

    public VeiculoServiceImpl(VeiculoRepositoryPort veiculoRepository) {
        this.veiculoRepository = veiculoRepository;
    }

    @Override
    public VeiculoOutput createVeiculo(CreateVeiculoCommand command) {
        if (veiculoRepository.existsByPlaca(command.placa())) {
            throw new VeiculoAlreadyExistsException(command.placa());
        }
        Veiculo veiculo = Veiculo.create(command.placa(), command.modelo(), command.tipo(), command.ano());
        return toOutput(veiculoRepository.save(veiculo));
    }

    @Override
    public VeiculoOutput getVeiculoById(Long id) {
        return toOutput(veiculoRepository.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id)));
    }

    @Override
    public List<VeiculoOutput> getAllVeiculos() {
        return veiculoRepository.findAll().stream().map(this::toOutput).toList();
    }

    @Override
    public Page<VeiculoOutput> getAllVeiculos(Pageable pageable) {
        return veiculoRepository.findAll(pageable).map(this::toOutput);
    }

    @Override
    public VeiculoOutput updateVeiculo(Long id, CreateVeiculoCommand command) {
        Veiculo existing = veiculoRepository.findById(id)
                .orElseThrow(() -> new VeiculoNotFoundException(id));

        veiculoRepository.findByPlaca(command.placa())
                .ifPresent(v -> {
                    if (!v.getId().equals(existing.getId())) {
                        throw new VeiculoAlreadyExistsException(command.placa());
                    }
                });

        Veiculo updated = Veiculo.reconstitute(id, command.placa(), command.modelo(),
                command.tipo(), command.ano());
        return toOutput(veiculoRepository.save(updated));
    }

    @Override
    public void deleteVeiculo(Long id) {
        if (!veiculoRepository.findById(id).isPresent()) {
            throw new VeiculoNotFoundException(id);
        }
        veiculoRepository.deleteById(id);
    }

    private VeiculoOutput toOutput(Veiculo v) {
        return new VeiculoOutput(v.getId(), v.getPlaca(), v.getModelo(), v.getTipo(), v.getAno());
    }
}
