package com.danieldiego.trackMaintenance.adapter.controller;

import com.danieldiego.trackMaintenance.adapter.dto.ApiResponse;
import com.danieldiego.trackMaintenance.adapter.dto.PagedApiResponse;
import com.danieldiego.trackMaintenance.adapter.dto.manutencao.CreateManutencaoRequest;
import com.danieldiego.trackMaintenance.adapter.dto.manutencao.UpdateManutencaoRequest;
import com.danieldiego.trackMaintenance.adapter.dto.manutencao.UpdateManutencaoStatusRequest;
import com.danieldiego.trackMaintenance.application.dto.manutencao.CreateManutencaoCommand;
import com.danieldiego.trackMaintenance.application.dto.manutencao.ManutencaoOutput;
import com.danieldiego.trackMaintenance.application.dto.manutencao.UpdateManutencaoCommand;
import com.danieldiego.trackMaintenance.application.service.manutencao.ManutencaoService;
import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/manutencoes")
@Tag(name = "Manutenções", description = "Endpoints para gerenciamento de manutenções")
@SecurityRequirement(name = "Bearer Authentication")
public class ManutencaoController {

    private final ManutencaoService manutencaoService;

    public ManutencaoController(ManutencaoService manutencaoService) {
        this.manutencaoService = manutencaoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Agendar manutenção", description = "Cria um novo agendamento de manutenção para um veículo")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Manutenção agendada com sucesso",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    public ApiResponse<ManutencaoOutput> create(@Valid @RequestBody CreateManutencaoRequest request) {
        CreateManutencaoCommand command = new CreateManutencaoCommand(
                request.veiculoId(),
                request.dataInicio(),
                request.dataFinalizacao(),
                request.tipoServico(),
                request.custoEstimado()
        );
        return ApiResponse.success(201, "Manutenção agendada com sucesso", manutencaoService.createManutencao(command));
    }

    @GetMapping
    @Operation(summary = "Listar manutenções", description = "Retorna manutenções com paginação")
    public PagedApiResponse<ManutencaoOutput> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return PagedApiResponse.of(200, "Manutenções listadas com sucesso", manutencaoService.getAllManutencoes(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar manutenção por ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Manutenção encontrada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    public ApiResponse<ManutencaoOutput> findById(@PathVariable Long id) {
        return ApiResponse.success(200, "Manutenção encontrada", manutencaoService.getManutencaoById(id));
    }

    @GetMapping("/veiculo/{veiculoId}")
    @Operation(summary = "Listar manutenções por veículo")
    public ApiResponse<List<ManutencaoOutput>> findByVeiculo(@PathVariable Long veiculoId) {
        return ApiResponse.success(200, "Manutenções do veículo listadas",
                manutencaoService.getManutencoesByVeiculo(veiculoId));
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Listar manutenções por status", description = "Valores: PENDENTE, EM_REALIZACAO, CONCLUIDA")
    public ApiResponse<List<ManutencaoOutput>> findByStatus(@PathVariable StatusManutencao status) {
        return ApiResponse.success(200, "Manutenções filtradas por status",
                manutencaoService.getManutencoesByStatus(status));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar manutenção", description = "Atualiza todos os campos de uma manutenção")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Manutenção atualizada"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    public ApiResponse<ManutencaoOutput> update(@PathVariable Long id,
                                                 @Valid @RequestBody UpdateManutencaoRequest request) {
        UpdateManutencaoCommand command = new UpdateManutencaoCommand(
                request.dataInicio(),
                request.dataFinalizacao(),
                request.tipoServico(),
                request.custoEstimado(),
                request.status()
        );
        return ApiResponse.success(200, "Manutenção atualizada com sucesso",
                manutencaoService.updateManutencao(id, command));
    }

    @PatchMapping("/{id}/status")
    @Operation(summary = "Atualizar status de manutenção", description = "Atualiza apenas o status da manutenção")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status atualizado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    public ApiResponse<ManutencaoOutput> updateStatus(@PathVariable Long id,
                                                       @Valid @RequestBody UpdateManutencaoStatusRequest request) {
        return ApiResponse.success(200, "Status atualizado com sucesso",
                manutencaoService.updateStatus(id, request.status()));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover manutenção")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Manutenção removida"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Manutenção não encontrada")
    })
    public void delete(@PathVariable Long id) {
        manutencaoService.deleteManutencao(id);
    }
}
