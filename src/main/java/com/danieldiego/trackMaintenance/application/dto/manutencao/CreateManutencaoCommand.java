package com.danieldiego.trackMaintenance.application.dto.manutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CreateManutencaoCommand(
        Long veiculoId,
        LocalDate dataInicio,
        LocalDate dataFinalizacao,
        String tipoServico,
        BigDecimal custoEstimado
) {
}
