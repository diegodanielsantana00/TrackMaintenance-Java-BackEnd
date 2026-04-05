package com.danieldiego.trackMaintenance.application.dto.dashboard;

import java.math.BigDecimal;

public record VeiculoPorCategoriaOutput(
        String tipo,
        long quantidade,
        BigDecimal percentual
) {
}
