package br.com.controle.financeiro.model.exception;

public class InstitutionNotFoundException extends NotFoundException {

	public InstitutionNotFoundException(Long id) {
		super("Could not find institution " + id);
	}

}
