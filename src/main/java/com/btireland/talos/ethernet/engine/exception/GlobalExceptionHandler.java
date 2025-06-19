package com.btireland.talos.ethernet.engine.exception;

import com.btireland.talos.core.common.rest.exception.BaseGlobalExceptionHandler;
import com.btireland.talos.ethernet.engine.exception.ordermanager.OrderManagerServiceBadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler extends BaseGlobalExceptionHandler {

	public GlobalExceptionHandler(ServerProperties serverProperties) {
		super(serverProperties);
	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public ResponseEntity<Object> handleDataIntegrityViolationException(WebRequest request, Exception ex){
		log.error("Data integrity violation caught", ex);
		return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

	@ExceptionHandler(BadRequestException.class)
	public ResponseEntity<Object> handleBadRequestException(WebRequest request, Exception ex){
		log.debug("BadRequestException caught", ex);
		return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
	}

    @ExceptionHandler(OrderManagerServiceBadRequestException.class)
    public ResponseEntity<Object> handleOrderManagerBadRequestException(WebRequest request, Exception ex) {
        log.debug("OrderManagerServiceBadRequestException caught", ex);
        return handleExceptionInternal(ex, null, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }
}
