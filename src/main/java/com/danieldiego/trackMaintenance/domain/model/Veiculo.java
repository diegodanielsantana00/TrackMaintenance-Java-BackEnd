package com.danieldiego.trackMaintenance.domain.model;

public class Veiculo {

    private static final String PLACA_REGEX = "^[A-Z]{3}-?\\d{4}$|^[A-Z]{3}\\d[A-Z]\\d{2}$";

    private Long id;
    private String placa;
    private String modelo;
    private TipoVeiculo tipo;
    private Integer ano;

    private Veiculo() {}

    private static void validarPlaca(String placa) {
        if (placa == null || !placa.matches(PLACA_REGEX)) {
            throw new IllegalArgumentException(
                    "Placa inválida: deve estar no formato antigo (ABC-1234) ou Mercosul (ABC1D23)");
        }
    }

    public static Veiculo create(String placa, String modelo, TipoVeiculo tipo, Integer ano) {
        validarPlaca(placa);
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
