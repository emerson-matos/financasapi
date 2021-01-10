package br.com.controle.financeiro.controller;

import br.com.controle.financeiro.model.exception.NotFoundException;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    private final HttpHeaders headers = new HttpHeaders();

    @ResponseBody
    @ExceptionHandler({NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    ResponseEntity<Object> objectNotFoundHandler(NotFoundException ex, WebRequest request) {
        logException(ex);
        return handleExceptionInternal(ex, null, headers, HttpStatus.NOT_FOUND, request);
    }

    @ResponseBody
    @ExceptionHandler({EmptyResultDataAccessException.class})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    ResponseEntity<Object> objectNotFoundHandler(EmptyResultDataAccessException ex, WebRequest request) {
        logException(ex);
        return handleExceptionInternal(ex, null, headers, HttpStatus.NO_CONTENT, request);
    }

    @ResponseBody
    @ExceptionHandler({AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    ResponseEntity<Object> forbidden(AccessDeniedException ex, WebRequest request) {
        logException(ex);
        return handleExceptionInternal(ex, ex.getCause(), headers, HttpStatus.FORBIDDEN, request);
    }

    //TODO NoSuchElementException

    private void logException(RuntimeException ex) {
        logger.info("handleException");
        logger.error(ex.getMessage(), ex);
    }

}
