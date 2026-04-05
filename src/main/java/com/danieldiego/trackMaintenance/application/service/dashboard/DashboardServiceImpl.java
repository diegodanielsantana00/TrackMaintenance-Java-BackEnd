package com.danieldiego.trackMaintenance.application.service.dashboard;

import com.danieldiego.trackMaintenance.application.Interface.dashboard.DashboardRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.dashboard.*;

import java.time.LocalDate;
import java.util.List;

public class DashboardServiceImpl implements DashboardService {

    private final DashboardRepositoryPort dashboardRepository;

    public DashboardServiceImpl(DashboardRepositoryPort dashboardRepository) {
        this.dashboardRepository = dashboardRepository;
    }

    @Override
    public TotalKmOutput getTotalKm(Long veiculoId) {
        return dashboardRepository.getTotalKm(veiculoId);
    }

    @Override
    public List<VeiculoPorCategoriaOutput> getVeiculosPorCategoria() {
        return dashboardRepository.getVeiculosPorCategoria();
    }

    @Override
    public List<CronogramaManutencaoOutput> getCronogramaManutencao() {
        return dashboardRepository.getCronogramaManutencao(5);
    }

    @Override
    public List<RankingUtilizacaoOutput> getRankingUtilizacao() {
        return dashboardRepository.getRankingUtilizacao(5);
    }

    @Override
    public ProjecaoFinanceiraOutput getProjecaoFinanceira() {
        LocalDate now = LocalDate.now();
        return dashboardRepository.getProjecaoFinanceira(now.getMonthValue(), now.getYear());
    }
}
