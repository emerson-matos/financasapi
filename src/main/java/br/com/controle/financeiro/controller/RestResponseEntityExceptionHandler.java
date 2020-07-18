package br.com.controle.financeiro.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.controle.financeiro.model.exception.BankAccountNotFoundException;
import br.com.controle.financeiro.model.exception.CardNotFoundException;
import br.com.controle.financeiro.model.exception.ClientNotFoundException;
import br.com.controle.financeiro.model.exception.InstitutionNotFoundException;
import br.com.controle.financeiro.model.exception.NotFoundException;
import br.com.controle.financeiro.model.exception.TransactionNotFoundException;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

	private static final Logger log = LoggerFactory.getLogger(RestResponseEntityExceptionHandler.class);

	@ResponseBody
	@ExceptionHandler({ ClientNotFoundException.class, InstitutionNotFoundException.class, CardNotFoundException.class,
			BankAccountNotFoundException.class, TransactionNotFoundException.class })
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<Object> objectNotFoundHandler(NotFoundException ex) {
		log.info("handleException");
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, null, null, HttpStatus.NOT_FOUND, null);
	}

	@ResponseBody
	@ExceptionHandler({ EmptyResultDataAccessException.class })
	@ResponseStatus(HttpStatus.NO_CONTENT)
	ResponseEntity<Object> objectNotFoundHandler(EmptyResultDataAccessException ex) {
		log.info("handleException");
		log.error(ex.getMessage(), ex);
		return handleExceptionInternal(ex, null, null, HttpStatus.NO_CONTENT, null);
	}
}
