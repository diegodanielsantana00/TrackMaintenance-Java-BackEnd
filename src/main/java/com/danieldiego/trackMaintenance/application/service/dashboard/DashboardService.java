package com.danieldiego.trackMaintenance.application.service.dashboard;

import com.danieldiego.trackMaintenance.application.dto.dashboard.*;

import java.util.List;

public interface DashboardService {

    TotalKmOutput getTotalKm(Long veiculoId);

    List<VeiculoPorCategoriaOutput> getVeiculosPorCategoria();

    List<CronogramaManutencaoOutput> getCronogramaManutencao();

    List<RankingUtilizacaoOutput> getRankingUtilizacao();

    ProjecaoFinanceiraOutput getProjecaoFinanceira();
}
