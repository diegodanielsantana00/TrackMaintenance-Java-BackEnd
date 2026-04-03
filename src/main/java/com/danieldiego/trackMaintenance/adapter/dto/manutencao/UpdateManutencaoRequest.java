package com.danieldiego.trackMaintenance.adapter.dto.manutencao;

import com.danieldiego.trackMaintenance.domain.model.StatusManutencao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

@Schema(description = "Payload para atualização de manutenção")
public record UpdateManutencaoRequest(

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
        BigDecimal custoEstimado,

        @Schema(description = "Status da manutenção", example = "EM_REALIZACAO")
        @NotNull(message = "Status é obrigatório")
        StatusManutencao status
) {
}
