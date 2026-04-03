package com.danieldiego.trackMaintenance.application.dto.veiculo;

import com.danieldiego.trackMaintenance.domain.model.TipoVeiculo;

public record CreateVeiculoCommand(String placa, String modelo, TipoVeiculo tipo, Integer ano) {
}
