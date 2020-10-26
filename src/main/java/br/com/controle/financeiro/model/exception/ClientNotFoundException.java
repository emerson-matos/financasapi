package br.com.controle.financeiro.model.exception;

import java.util.UUID;

public class ClientNotFoundException extends NotFoundException {

    public ClientNotFoundException(UUID id) {
        super("Could not find client " + id.toString());
    }

}
