package com.danieldiego.trackMaintenance.domain.exception;

public class ManutencaoNotFoundException extends BusinessException {

    public ManutencaoNotFoundException(Long id) {
        super("Manutencao with id '" + id + "' not found");
    }
}
