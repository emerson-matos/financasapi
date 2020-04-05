package br.com.controle.financeiro.model.exception;

public class NotFoundException extends RuntimeException {

	public NotFoundException(Long id) {
		super("Could not find resource with" + id);
    }
    
    public NotFoundException(String message) {
		super(message);
	}
}
