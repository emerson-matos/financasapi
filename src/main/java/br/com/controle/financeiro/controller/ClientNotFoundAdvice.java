package br.com.controle.financeiro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.controle.financeiro.model.exception.ClientNotFoundException;

@ControllerAdvice
public class ClientNotFoundAdvice {
	
	@ResponseBody
	@ExceptionHandler(ClientNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	String clientNotFoundHandler(ClientNotFoundException e) {
		return e.getMessage();
	}
}
