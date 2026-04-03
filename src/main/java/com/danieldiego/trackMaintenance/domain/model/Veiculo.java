package com.danieldiego.trackMaintenance.domain.model;

public class Veiculo {

    private Long id;
    private String placa;
    private String modelo;
    private TipoVeiculo tipo;
    private Integer ano;

    private Veiculo() {}

    public static Veiculo create(String placa, String modelo, TipoVeiculo tipo, Integer ano) {
        Veiculo v = new Veiculo();
        v.placa  = placa;
        v.modelo = modelo;
        v.tipo   = tipo;
        v.ano    = ano;
        return v;
    }

    public static Veiculo reconstitute(Long id, String placa, String modelo, TipoVeiculo tipo, Integer ano) {
        Veiculo v = new Veiculo();
        v.id     = id;
        v.placa  = placa;
        v.modelo = modelo;
        v.tipo   = tipo;
        v.ano    = ano;
        return v;
    }

    public Long getId()          { return id; }
    public String getPlaca()     { return placa; }
    public String getModelo()    { return modelo; }
    public TipoVeiculo getTipo() { return tipo; }
    public Integer getAno()      { return ano; }
}
