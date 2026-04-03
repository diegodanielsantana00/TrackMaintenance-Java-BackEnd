package com.danieldiego.trackMaintenance.adapter.dto.manutencao;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Payload para criação de manutenção")
public record CreateManutencaoRequest(

        @Schema(description = "ID do veículo", example = "1")
        @NotNull(message = "ID do veículo é obrigatório")
        Long veiculoId,

        @Schema(description = "Data de início da manutenção", example = "2024-06-10")
        @NotNull(message = "Data de início é obrigatória")
        LocalDate dataInicio,

        @Schema(description = "Data prevista de finalização", example = "2024-06-11")
        LocalDate dataFinalizacao,

        @Schema(description = "Tipo de serviço", example = "Troca de Óleo")
        @NotBlank(message = "Tipo de serviço é obrigatório")
        String tipoServico,

        @Schema(description = "Custo estimado", example = "350.00")
        @DecimalMin(value = "0.0", inclusive = false, message = "Custo estimado deve ser maior que zero")
        BigDecimal custoEstimado
) {
}
