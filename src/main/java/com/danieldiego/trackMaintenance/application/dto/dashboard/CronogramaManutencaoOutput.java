package com.danieldiego.trackMaintenance.application.dto.dashboard;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CronogramaManutencaoOutput(
        Long id,
        String veiculoPlaca,
        String veiculoModelo,
        String tipoServico,
        LocalDate dataInicio,
        LocalDate dataFinalizacao,
        BigDecimal custoEstimado,
        String status
) {
}
