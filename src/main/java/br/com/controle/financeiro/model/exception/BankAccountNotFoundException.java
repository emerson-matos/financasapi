package br.com.controle.financeiro.model.exception;

import java.util.UUID;

public class BankAccountNotFoundException extends NotFoundException {

    public BankAccountNotFoundException(UUID id) {
        super("Could not find BankAccount " + id);
    }

}
