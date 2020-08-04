package br.com.controle.financeiro.model.exception;

import java.util.UUID;

public class TransactionNotFoundException extends NotFoundException {

    public TransactionNotFoundException(UUID id) {
        super("Could not find transaction " + id);
    }

}
