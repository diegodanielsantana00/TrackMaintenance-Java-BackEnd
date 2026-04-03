package com.danieldiego.trackMaintenance.infrastructure.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "viagens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ViagemJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veiculo_id")
    private VeiculoJpaEntity veiculo;

    @Column(name = "data_saida", nullable = false)
    private LocalDateTime dataSaida;

    @Column(name = "data_chegada")
    private LocalDateTime dataChegada;

    @Column(name = "origem", length = 100)
    private String origem;

    @Column(name = "destino", length = 100)
    private String destino;

    @Column(name = "km_percorrida", precision = 10, scale = 2)
    private BigDecimal kmPercorrida;
}
