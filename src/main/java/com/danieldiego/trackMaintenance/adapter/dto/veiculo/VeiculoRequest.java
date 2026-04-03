package com.danieldiego.trackMaintenance.adapter.dto.veiculo;

import com.danieldiego.trackMaintenance.domain.model.TipoVeiculo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Schema(description = "Veiculo request payload")
public record VeiculoRequest(

        @Schema(description = "Placa do veículo", example = "ABC-1234")
        @NotBlank(message = "Placa é obrigatória")
        @Size(max = 10, message = "Placa deve ter no máximo 10 caracteres")
        String placa,

        @Schema(description = "Modelo do veículo", example = "Fiorino")
        @NotBlank(message = "Modelo é obrigatório")
        @Size(max = 50, message = "Modelo deve ter no máximo 50 caracteres")
        String modelo,

        @Schema(description = "Tipo do veículo: LEVE ou PESADO", example = "LEVE")
        @NotNull(message = "Tipo é obrigatório")
        TipoVeiculo tipo,

        @Schema(description = "Ano de fabricação", example = "2022")
        Integer ano
) {
}
