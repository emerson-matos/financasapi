package br.com.controle.financeiro.model.exception;

public class CardNotFoundException extends NotFoundException {

    public CardNotFoundException(Long id) {
        super("Could not find card " + id);
    }

}
