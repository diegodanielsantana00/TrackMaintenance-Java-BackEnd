package com.danieldiego.trackMaintenance.application.dto.dashboard;

import java.math.BigDecimal;

public record RankingUtilizacaoOutput(
        Long veiculoId,
        String placa,
        String modelo,
        String tipo,
        long totalViagens,
        BigDecimal totalKm
) {
}
