package com.danieldiego.trackMaintenance.application.Interface.dashboard;

import com.danieldiego.trackMaintenance.application.dto.dashboard.*;

import java.util.List;

public interface DashboardRepositoryPort {

    TotalKmOutput getTotalKm(Long veiculoId);

    List<VeiculoPorCategoriaOutput> getVeiculosPorCategoria();

    List<CronogramaManutencaoOutput> getCronogramaManutencao(int limit);

    List<RankingUtilizacaoOutput> getRankingUtilizacao(int limit);

    ProjecaoFinanceiraOutput getProjecaoFinanceira(int mes, int ano);
}
