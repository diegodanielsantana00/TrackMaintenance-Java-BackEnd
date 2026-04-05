package com.danieldiego.trackMaintenance.application.dto.veiculo;

import com.danieldiego.trackMaintenance.domain.enums.TipoVeiculo;

public record VeiculoOutput(Long id, String placa, String modelo, TipoVeiculo tipo, Integer ano) {
}
