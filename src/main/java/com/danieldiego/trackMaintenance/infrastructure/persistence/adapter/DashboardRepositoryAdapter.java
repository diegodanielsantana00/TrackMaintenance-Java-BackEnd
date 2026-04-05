package com.danieldiego.trackMaintenance.infrastructure.persistence.adapter;

import com.danieldiego.trackMaintenance.application.Interface.dashboard.DashboardRepositoryPort;
import com.danieldiego.trackMaintenance.application.dto.dashboard.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.List;

@Component
public class DashboardRepositoryAdapter implements DashboardRepositoryPort {

    private final JdbcTemplate jdbcTemplate;

    public DashboardRepositoryAdapter(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public TotalKmOutput getTotalKm(Long veiculoId) {
        if (veiculoId != null) {
            return jdbcTemplate.queryForObject("""
                    SELECT COALESCE(SUM(km_percorrida), 0) AS total_km
                    FROM viagens
                    WHERE veiculo_id = ?
                    """,
                    (rs, rowNum) -> new TotalKmOutput(rs.getBigDecimal("total_km")),
                    veiculoId
            );
        }
        return jdbcTemplate.queryForObject("""
                SELECT COALESCE(SUM(km_percorrida), 0) AS total_km
                FROM viagens
                """,
                (rs, rowNum) -> new TotalKmOutput(rs.getBigDecimal("total_km"))
        );
    }

    @Override
    public List<VeiculoPorCategoriaOutput> getVeiculosPorCategoria() {
        return jdbcTemplate.query("""
                SELECT
                    v.tipo,
                    COUNT(vi.id) AS quantidade,
                    ROUND(COUNT(vi.id) * 100.0 / NULLIF((SELECT COUNT(*) FROM viagens), 0), 1) AS percentual
                FROM viagens vi
                JOIN veiculos v ON v.id = vi.veiculo_id
                GROUP BY v.tipo
                ORDER BY quantidade DESC
                """,
                (rs, rowNum) -> new VeiculoPorCategoriaOutput(
                        rs.getString("tipo"),
                        rs.getLong("quantidade"),
                        rs.getBigDecimal("percentual") != null
                                ? rs.getBigDecimal("percentual")
                                : BigDecimal.ZERO
                )
        );
    }

    @Override
    public List<CronogramaManutencaoOutput> getCronogramaManutencao(int limit) {
        return jdbcTemplate.query("""
                SELECT
                    m.id,
                    v.placa AS veiculo_placa,
                    v.modelo AS veiculo_modelo,
                    m.tipo_servico,
                    m.data_inicio,
                    m.data_finalizacao,
                    m.custo_estimado,
                    m.status
                FROM manutencoes m
                JOIN veiculos v ON v.id = m.veiculo_id
                WHERE m.status IN ('PENDENTE', 'EM_REALIZACAO')
                ORDER BY m.data_inicio ASC
                LIMIT ?
                """,
                (rs, rowNum) -> {
                    Date dataFin = rs.getDate("data_finalizacao");
                    return new CronogramaManutencaoOutput(
                            rs.getLong("id"),
                            rs.getString("veiculo_placa"),
                            rs.getString("veiculo_modelo"),
                            rs.getString("tipo_servico"),
                            rs.getDate("data_inicio").toLocalDate(),
                            dataFin != null ? dataFin.toLocalDate() : null,
                            rs.getBigDecimal("custo_estimado"),
                            rs.getString("status")
                    );
                },
                limit
        );
    }

    @Override
    public List<RankingUtilizacaoOutput> getRankingUtilizacao(int limit) {
        return jdbcTemplate.query("""
                SELECT
                    v.id AS veiculo_id,
                    v.placa,
                    v.modelo,
                    v.tipo,
                    COUNT(vi.id) AS total_viagens,
                    COALESCE(SUM(vi.km_percorrida), 0) AS total_km
                FROM veiculos v
                LEFT JOIN viagens vi ON vi.veiculo_id = v.id
                GROUP BY v.id, v.placa, v.modelo, v.tipo
                ORDER BY total_km DESC
                LIMIT ?
                """,
                (rs, rowNum) -> new RankingUtilizacaoOutput(
                        rs.getLong("veiculo_id"),
                        rs.getString("placa"),
                        rs.getString("modelo"),
                        rs.getString("tipo"),
                        rs.getLong("total_viagens"),
                        rs.getBigDecimal("total_km")
                ),
                limit
        );
    }

    @Override
    public ProjecaoFinanceiraOutput getProjecaoFinanceira(int mes, int ano) {
        return jdbcTemplate.queryForObject("""
                SELECT
                    COALESCE(SUM(custo_estimado), 0) AS custo_total,
                    COUNT(*) AS total_manutencoes
                FROM manutencoes
                WHERE EXTRACT(MONTH FROM data_inicio) = ?
                  AND EXTRACT(YEAR FROM data_inicio) = ?
                """,
                (rs, rowNum) -> new ProjecaoFinanceiraOutput(
                        mes,
                        ano,
                        rs.getBigDecimal("custo_total"),
                        rs.getLong("total_manutencoes")
                ),
                mes, ano
        );
    }
}
