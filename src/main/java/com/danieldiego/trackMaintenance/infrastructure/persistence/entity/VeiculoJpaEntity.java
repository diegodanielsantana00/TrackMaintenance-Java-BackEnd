package com.danieldiego.trackMaintenance.infrastructure.persistence.entity;

import com.danieldiego.trackMaintenance.domain.enums.TipoVeiculo;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "veiculos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VeiculoJpaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "placa", nullable = false, unique = true, length = 10)
    private String placa;

    @Column(name = "modelo", nullable = false, length = 50)
    private String modelo;

    @Enumerated(EnumType.STRING)
    @Column(name = "tipo", length = 20)
    private TipoVeiculo tipo;

    @Column(name = "ano")
    private Integer ano;
}
