package com.danieldiego.trackMaintenance.adapter.controller;

import com.danieldiego.trackMaintenance.adapter.dto.ApiResponse;
import com.danieldiego.trackMaintenance.adapter.dto.PagedApiResponse;
import com.danieldiego.trackMaintenance.adapter.dto.veiculo.VeiculoRequest;
import com.danieldiego.trackMaintenance.application.dto.veiculo.CreateVeiculoCommand;
import com.danieldiego.trackMaintenance.application.dto.veiculo.VeiculoOutput;
import com.danieldiego.trackMaintenance.application.service.veiculo.VeiculoService;
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

@RestController
@RequestMapping("/v1/veiculos")
@Tag(name = "Veículos", description = "Endpoints para gerenciamento de veículos")
@SecurityRequirement(name = "Bearer Authentication")
public class VeiculoController {

    private final VeiculoService veiculoService;

    public VeiculoController(VeiculoService veiculoService) {
        this.veiculoService = veiculoService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Criar veículo", description = "Cadastra um novo veículo na frota")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Veículo criado com sucesso",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class))),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Placa já cadastrada",
                    content = @Content(schema = @Schema(implementation = ApiResponse.class)))
    })
    public ApiResponse<VeiculoOutput> create(@Valid @RequestBody VeiculoRequest request) {
        CreateVeiculoCommand command = new CreateVeiculoCommand(
                request.placa(), request.modelo(), request.tipo(), request.ano());
        return ApiResponse.success(201, "Veículo criado com sucesso", veiculoService.createVeiculo(command));
    }

    @GetMapping
    @Operation(summary = "Listar veículos", description = "Retorna veículos com paginação")
    public PagedApiResponse<VeiculoOutput> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        var pageable = PageRequest.of(page, size, Sort.by("id").descending());
        return PagedApiResponse.of(200, "Veículos listados com sucesso", veiculoService.getAllVeiculos(pageable));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar veículo por ID")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Veículo encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    public ApiResponse<VeiculoOutput> findById(@PathVariable Long id) {
        return ApiResponse.success(200, "Veículo encontrado", veiculoService.getVeiculoById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Atualizar veículo", description = "Atualiza os dados de um veículo existente")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Veículo atualizado com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Veículo não encontrado"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "409", description = "Placa já em uso")
    })
    public ApiResponse<VeiculoOutput> update(@PathVariable Long id, @Valid @RequestBody VeiculoRequest request) {
        CreateVeiculoCommand command = new CreateVeiculoCommand(
                request.placa(), request.modelo(), request.tipo(), request.ano());
        return ApiResponse.success(200, "Veículo atualizado com sucesso", veiculoService.updateVeiculo(id, command));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Remover veículo")
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Veículo removido com sucesso"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Veículo não encontrado")
    })
    public void delete(@PathVariable Long id) {
        veiculoService.deleteVeiculo(id);
    }
}
