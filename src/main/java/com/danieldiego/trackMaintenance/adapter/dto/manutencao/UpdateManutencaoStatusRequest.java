package com.danieldiego.trackMaintenance.adapter.dto.manutencao;

import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload para atualização de status de manutenção")
public record UpdateManutencaoStatusRequest(

        @Schema(description = "Novo status da manutenção", example = "EM_REALIZACAO")
        @NotNull(message = "Status é obrigatório")
        StatusManutencao status
) {
}
