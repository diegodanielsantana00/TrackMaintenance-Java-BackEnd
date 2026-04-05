package com.danieldiego.trackMaintenance.infrastructure.persistence.adapter;

import com.danieldiego.trackMaintenance.application.Interface.manutencao.ManutencaoRepositoryPort;
import com.danieldiego.trackMaintenance.domain.model.Manutencao;
import com.danieldiego.trackMaintenance.domain.model.StatusManutencao;
import com.danieldiego.trackMaintenance.infrastructure.persistence.entity.ManutencaoJpaEntity;
import com.danieldiego.trackMaintenance.infrastructure.persistence.entity.VeiculoJpaEntity;
import com.danieldiego.trackMaintenance.infrastructure.persistence.repository.ManutencaoJpaRepository;
import com.danieldiego.trackMaintenance.infrastructure.persistence.repository.VeiculoJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class ManutencaoRepositoryAdapter implements ManutencaoRepositoryPort {

    private final ManutencaoJpaRepository jpaRepository;
    private final VeiculoJpaRepository veiculoJpaRepository;

    public ManutencaoRepositoryAdapter(ManutencaoJpaRepository jpaRepository,
                                       VeiculoJpaRepository veiculoJpaRepository) {
        this.jpaRepository = jpaRepository;
        this.veiculoJpaRepository = veiculoJpaRepository;
    }

    @Override
    public Manutencao save(Manutencao manutencao) {
        VeiculoJpaEntity veiculoEntity = veiculoJpaRepository.getReferenceById(manutencao.getVeiculoId());
        ManutencaoJpaEntity entity = toJpaEntity(manutencao, veiculoEntity);
        return toDomain(jpaRepository.save(entity));
    }

    @Override
    public Optional<Manutencao> findById(Long id) {
        return jpaRepository.findById(id).map(this::toDomain);
    }

    @Override
    public List<Manutencao> findAll() {
        return jpaRepository.findAll().stream().map(this::toDomain).toList();
    }

    @Override
    public Page<Manutencao> findAll(Pageable pageable) {
        return jpaRepository.findAll(pageable).map(this::toDomain);
    }

    @Override
    public List<Manutencao> findByVeiculoId(Long veiculoId) {
        return jpaRepository.findByVeiculoId(veiculoId).stream().map(this::toDomain).toList();
    }

    @Override
    public List<Manutencao> findByStatus(StatusManutencao status) {
        return jpaRepository.findByStatus(status).stream().map(this::toDomain).toList();
    }

    @Override
    public boolean existsActiveByVeiculoId(Long veiculoId) {
        return jpaRepository.existsByVeiculoIdAndStatusIn(
                veiculoId, List.of(StatusManutencao.PENDENTE, StatusManutencao.EM_REALIZACAO));
    }

    @Override
    public void deleteById(Long id) {
        jpaRepository.deleteById(id);
    }

    private ManutencaoJpaEntity toJpaEntity(Manutencao m, VeiculoJpaEntity veiculoEntity) {
        return ManutencaoJpaEntity.builder()
                .id(m.getId())
                .veiculo(veiculoEntity)
                .dataInicio(m.getDataInicio())
                .dataFinalizacao(m.getDataFinalizacao())
                .tipoServico(m.getTipoServico())
                .custoEstimado(m.getCustoEstimado())
                .status(m.getStatus())
                .build();
    }

    private Manutencao toDomain(ManutencaoJpaEntity e) {
        return Manutencao.reconstitute(
                e.getId(),
                e.getVeiculo().getId(),
                e.getDataInicio(),
                e.getDataFinalizacao(),
                e.getTipoServico(),
                e.getCustoEstimado(),
                e.getStatus()
        );
    }
}
