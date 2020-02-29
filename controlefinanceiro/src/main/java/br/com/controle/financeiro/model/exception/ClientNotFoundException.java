package br.com.controle.financeiro.model.exception;

public class ClientNotFoundException extends RuntimeException {
	
	public ClientNotFoundException(Long id) {
		super("Could not find employee " + id);
	}
	
}
