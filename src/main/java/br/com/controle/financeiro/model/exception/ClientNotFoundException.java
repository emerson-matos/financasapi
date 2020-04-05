package br.com.controle.financeiro.model.exception;

public class ClientNotFoundException extends NotFoundException  {

	public ClientNotFoundException(Long id) {
		super("Could not find client " + id);
	}

}
