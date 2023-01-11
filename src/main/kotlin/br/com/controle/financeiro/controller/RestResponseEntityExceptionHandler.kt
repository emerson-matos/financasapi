package br.com.controle.financeiro.controller

import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.annotation.ResponseBody
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.dao.EmptyResultDataAccessException
import br.com.controle.financeiro.model.exception.NotFoundException

@RestControllerAdvice
class RestResponseEntityExceptionHandler : ResponseEntityExceptionHandler() {
    private val headers = HttpHeaders()

    @ResponseBody
    @ExceptionHandler(NotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun objectNotFoundHandler(ex: NotFoundException, request: WebRequest): ResponseEntity<Any>? {
        logException(ex)
        return handleExceptionInternal(ex, null, headers, HttpStatus.NOT_FOUND, request)
    }

    @ResponseBody
    @ExceptionHandler(EmptyResultDataAccessException::class)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun objectNotFoundHandler(ex: EmptyResultDataAccessException, request: WebRequest): ResponseEntity<Any>? {
        logException(ex)
        return handleExceptionInternal(ex, null, headers, HttpStatus.NO_CONTENT, request)
    }

    //TODO NoSuchElementException
    private fun logException(ex: RuntimeException) {
        logger.info("handleException")
        logger.error(ex.message, ex)
    }
}
