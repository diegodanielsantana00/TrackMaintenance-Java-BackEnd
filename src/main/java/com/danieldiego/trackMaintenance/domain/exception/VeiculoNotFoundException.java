package com.danieldiego.trackMaintenance.domain.exception;

public class VeiculoNotFoundException extends BusinessException {

    public VeiculoNotFoundException(Long id) {
        super("Veiculo with id '" + id + "' not found");
    }
}
