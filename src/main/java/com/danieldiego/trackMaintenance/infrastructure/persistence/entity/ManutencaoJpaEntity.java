package com.danieldiego.trackMaintenance.infrastructure.persistence.entity;

import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "manutencoes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ManutencaoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "veiculo_id", nullable = false)
    private VeiculoJpaEntity veiculo;

    @Column(name = "data_inicio", nullable = false)
    private LocalDate dataInicio;

    @Column(name = "data_finalizacao")
    private LocalDate dataFinalizacao;

    @Column(name = "tipo_servico", length = 100)
    private String tipoServico;

    @Column(name = "custo_estimado", precision = 10, scale = 2)
    private BigDecimal custoEstimado;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", length = 20)
    private StatusManutencao status;
}
