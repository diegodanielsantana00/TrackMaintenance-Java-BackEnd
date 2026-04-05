package com.danieldiego.trackMaintenance.domain.model;

import com.danieldiego.trackMaintenance.domain.enums.StatusManutencao;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Manutencao {

    private static final BigDecimal CUSTO_MAXIMO = new BigDecimal("1000000.00");

    private Long id;
    private Long veiculoId;
    private LocalDate dataInicio;
    private LocalDate dataFinalizacao;
    private String tipoServico;
    private BigDecimal custoEstimado;
    private StatusManutencao status;

    private Manutencao() {}

    public static Manutencao create(Long veiculoId, LocalDate dataInicio, LocalDate dataFinalizacao,
                                    String tipoServico, BigDecimal custoEstimado) {
        validarCusto(custoEstimado);
        validarDatas(dataInicio, dataFinalizacao);

        Manutencao m = new Manutencao();
        m.veiculoId       = veiculoId;
        m.dataInicio      = dataInicio;
        m.dataFinalizacao = dataFinalizacao;
        m.tipoServico     = tipoServico;
        m.custoEstimado   = custoEstimado;
        m.status          = StatusManutencao.PENDENTE;
        return m;
    }

    public static Manutencao reconstitute(Long id, Long veiculoId, LocalDate dataInicio,
                                          LocalDate dataFinalizacao, String tipoServico,
                                          BigDecimal custoEstimado, StatusManutencao status) {
        Manutencao m = new Manutencao();
        m.id              = id;
        m.veiculoId       = veiculoId;
        m.dataInicio      = dataInicio;
        m.dataFinalizacao = dataFinalizacao;
        m.tipoServico     = tipoServico;
        m.custoEstimado   = custoEstimado;
        m.status          = status;
        return m;
    }

    public void atualizarStatus(StatusManutencao novoStatus) {
        validarTransicaoStatus(this.status, novoStatus);
        this.status = novoStatus;
    }

    public void atualizar(LocalDate dataInicio, LocalDate dataFinalizacao,
                          String tipoServico, BigDecimal custoEstimado, StatusManutencao novoStatus) {
        validarCusto(custoEstimado);
        validarDatas(dataInicio, dataFinalizacao);
        validarTransicaoStatus(this.status, novoStatus);

        this.dataInicio      = dataInicio;
        this.dataFinalizacao = dataFinalizacao;
        this.tipoServico     = tipoServico;
        this.custoEstimado   = custoEstimado;
        this.status          = novoStatus;
    }

    public boolean isAtiva() {
        return this.status == StatusManutencao.PENDENTE || this.status == StatusManutencao.EM_REALIZACAO;
    }

    public boolean isEmAndamento() {
        return this.status == StatusManutencao.EM_REALIZACAO;
    }

    public static void validarCusto(BigDecimal custo) {
        if (custo != null) {
            if (custo.compareTo(BigDecimal.ZERO) < 0) {
                throw new IllegalArgumentException("Custo estimado não pode ser negativo");
            }
            if (custo.compareTo(CUSTO_MAXIMO) > 0) {
                throw new IllegalArgumentException("Custo estimado não pode exceder R$ 1.000.000,00");
            }
        }
    }

    public static void validarDatas(LocalDate dataInicio, LocalDate dataFinalizacao) {
        if (dataInicio != null && dataFinalizacao != null && dataFinalizacao.isBefore(dataInicio)) {
            throw new IllegalArgumentException("Data de finalização não pode ser anterior à data de início");
        }
    }

    public static void validarTransicaoStatus(StatusManutencao atual, StatusManutencao novo) {
        if (atual == novo) return;

        boolean valido = switch (atual) {
            case PENDENTE -> novo == StatusManutencao.EM_REALIZACAO;
            case EM_REALIZACAO -> novo == StatusManutencao.CONCLUIDA;
            case CONCLUIDA -> false;
        };

        if (!valido) {
            throw new IllegalStateException(
                    "Transição de status inválida: " + atual + " → " + novo +
                    ". Fluxo permitido: PENDENTE → EM_REALIZACAO → CONCLUIDA"
            );
        }
    }

    public Long getId()                     { return id; }
    public Long getVeiculoId()              { return veiculoId; }
    public LocalDate getDataInicio()        { return dataInicio; }
    public LocalDate getDataFinalizacao()   { return dataFinalizacao; }
    public String getTipoServico()          { return tipoServico; }
    public BigDecimal getCustoEstimado()    { return custoEstimado; }
    public StatusManutencao getStatus()     { return status; }
}
