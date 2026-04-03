package com.danieldiego.trackMaintenance.application.dto.manutencao;

import com.danieldiego.trackMaintenance.domain.model.StatusManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record UpdateManutencaoCommand(
        LocalDate dataInicio,
        LocalDate dataFinalizacao,
        String tipoServico,
        BigDecimal custoEstimado,
        StatusManutencao status
) {
}
