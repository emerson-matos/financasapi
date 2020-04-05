package br.com.controle.financeiro.model.exception;

public class BankAccountNotFoundException extends NotFoundException {

	public BankAccountNotFoundException(Long id) {
		super("Could not find BankAccount " + id);
	}

}
