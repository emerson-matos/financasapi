package br.com.controle.financeiro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.controle.financeiro.model.exception.ClientNotFoundException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler(ClientNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<Object> clientNotFoundHandler(ClientNotFoundException ex) {
		log.info("handleException");
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, null);
	}
}
