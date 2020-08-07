package br.com.controle.financeiro.model.exception;

import java.util.UUID;

public class CardNotFoundException extends NotFoundException {

    public CardNotFoundException(UUID id) {
        super("Could not find card " + id);
    }

}
