package com.adobe.bookstore.exceptions;

import com.fasterxml.jackson.core.JsonProcessingException;
import java.io.IOException;
import java.sql.SQLException;
import javax.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
@Slf4j
public class BookStoreExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler({IllegalArgumentException.class, UnsupportedOperationException.class, ConstraintViolationException.class})
    protected ResponseEntity<Object> handleInvalidRequestException(RuntimeException e, WebRequest request) {
        var bodyOfResponse = String.format("Invalid request. Please review parameters, payload, and resources: %s", e.getMessage());
        log.error("Invalid Request Exception Found: {}", bodyOfResponse, e);
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({IllegalStateException.class, IOException.class, SQLException.class, JsonProcessingException.class})
    protected ResponseEntity<Object> handleServerException(Exception e, WebRequest request) {
        var bodyOfResponse = String.format("Unexpected error: %s", e.getMessage());
        log.error("Server Exception Found: {}", bodyOfResponse, e);
        return handleExceptionInternal(e, bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request);
    }
}
