package br.com.controle.financeiro.model.exception;

public class TransactionNotFoundException extends NotFoundException {

    public TransactionNotFoundException(Long id) {
        super("Could not find transaction " + id);
    }

}
