package com.danieldiego.trackMaintenance.domain.exception;

public class VeiculoAlreadyExistsException extends BusinessException {

    public VeiculoAlreadyExistsException(String placa) {
        super("Veiculo with placa '" + placa + "' already exists");
    }
}
