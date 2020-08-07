package br.com.controle.financeiro.model.exception;

import java.util.UUID;

public class InstitutionNotFoundException extends NotFoundException {

    public InstitutionNotFoundException(UUID id) {
        super("Could not find institution " + id);
    }

}
