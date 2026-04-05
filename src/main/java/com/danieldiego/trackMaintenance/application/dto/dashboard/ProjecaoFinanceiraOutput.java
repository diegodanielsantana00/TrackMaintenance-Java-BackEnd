package com.danieldiego.trackMaintenance.application.dto.dashboard;

import java.math.BigDecimal;

public record ProjecaoFinanceiraOutput(
        int mes,
        int ano,
        BigDecimal custoTotal,
        long totalManutencoes
) {
}
