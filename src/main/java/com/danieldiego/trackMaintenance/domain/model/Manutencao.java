package com.danieldiego.trackMaintenance.domain.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Manutencao {

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

    public Long getId()                     { return id; }
    public Long getVeiculoId()              { return veiculoId; }
    public LocalDate getDataInicio()        { return dataInicio; }
    public LocalDate getDataFinalizacao()   { return dataFinalizacao; }
    public String getTipoServico()          { return tipoServico; }
    public BigDecimal getCustoEstimado()    { return custoEstimado; }
    public StatusManutencao getStatus()     { return status; }
}
