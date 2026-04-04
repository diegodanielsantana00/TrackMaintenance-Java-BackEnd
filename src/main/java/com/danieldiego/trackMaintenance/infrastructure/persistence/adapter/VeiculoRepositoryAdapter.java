package com.danieldiego.trackMaintenance.infrastructure.persistence.adapter;

import com.danieldiego.trackMaintenance.application.Interface.veiculo.VeiculoRepositoryPort;
import com.danieldiego.trackMaintenance.domain.model.Veiculo;
import com.danieldiego.trackMaintenance.infrastructure.persistence.entity.VeiculoJpaEntity;
import com.danieldiego.trackMaintenance.infrastructure.persistence.repository.VeiculoJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class VeiculoRepositoryAdapter implements VeiculoRepositoryPort {

    private final VeiculoJpaRepository jpaRepository;

    public VeiculoRepositoryAdapter(VeiculoJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public Veiculo save(Veiculo veiculo) {
        VeiculoJpaEntity entity = toJpaEntity(veiculo);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Veiculo> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public Optional<Veiculo> findByPlaca(String placa) {
        return jpaRepository.findByPlaca(placa).map(this::toDomain);
    }

    @Override
    public List<Veiculo> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsByPlaca(String placa) {
        return jpaRepository.existsByPlaca(placa);
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private VeiculoJpaEntity toJpaEntity(Veiculo v) {
        return VeiculoJpaEntity.builder()
                .id(v.getId())
                .placa(v.getPlaca())
                .modelo(v.getModelo())
                .tipo(v.getTipo())
                .ano(v.getAno())
                .build();
    }

    private Veiculo toDomain(VeiculoJpaEntity e) {
        return Veiculo.reconstitute(e.getId(), e.getPlaca(), e.getModelo(), e.getTipo(), e.getAno());
    }
}
