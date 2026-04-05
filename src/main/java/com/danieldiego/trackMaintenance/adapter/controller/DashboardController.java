package com.danieldiego.trackMaintenance.adapter.controller;

import com.danieldiego.trackMaintenance.adapter.dto.ApiResponse;
import com.danieldiego.trackMaintenance.application.dto.dashboard.*;
import com.danieldiego.trackMaintenance.application.service.dashboard.DashboardService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/dashboard")
@Tag(name = "Dashboard", description = "Endpoints de indicadores do dashboard")
@SecurityRequirement(name = "Bearer Authentication")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/total-km")
    @Operation(summary = "Total de KM percorrido", description = "Soma da quilometragem de um veículo específico ou de toda a frota")
    public ApiResponse<TotalKmOutput> getTotalKm(
            @RequestParam(required = false) Long veiculoId) {
        return ApiResponse.success(200, "Total de KM percorrido", dashboardService.getTotalKm(veiculoId));
    }

    @GetMapping("/volume-por-categoria")
    @Operation(summary = "Volume por categoria", description = "Quantidade de viagens realizadas por tipo de veículo (LEVE/PESADO)")
    public ApiResponse<List<VeiculoPorCategoriaOutput>> getVolumePorCategoria() {
        return ApiResponse.success(200, "Volume por categoria", dashboardService.getVeiculosPorCategoria());
    }

    @GetMapping("/cronograma-manutencao")
    @Operation(summary = "Cronograma de manutenção", description = "Próximas 5 manutenções pendentes ou em realização")
    public ApiResponse<List<CronogramaManutencaoOutput>> getCronogramaManutencao() {
        return ApiResponse.success(200, "Cronograma de manutenção", dashboardService.getCronogramaManutencao());
    }

    @GetMapping("/ranking-utilizacao")
    @Operation(summary = "Ranking de utilização", description = "Top 5 veículos por km percorrida")
    public ApiResponse<List<RankingUtilizacaoOutput>> getRankingUtilizacao() {
        return ApiResponse.success(200, "Ranking de utilização", dashboardService.getRankingUtilizacao());
    }

    @GetMapping("/projecao-financeira")
    @Operation(summary = "Projeção financeira", description = "Soma do custo total estimado em manutenções para o mês atual")
    public ApiResponse<ProjecaoFinanceiraOutput> getProjecaoFinanceira() {
        return ApiResponse.success(200, "Projeção financeira", dashboardService.getProjecaoFinanceira());
    }
}
