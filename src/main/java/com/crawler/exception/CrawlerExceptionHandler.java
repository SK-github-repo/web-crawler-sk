package com.crawler.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class CrawlerExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(IncorrectUrlException exception) {
        ErrorResponse errorResponse;

        errorResponse = buildErrorResponse(exception, HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorResponse> handleException(Exception exception) {
        ErrorResponse errorResponse;

        errorResponse = buildErrorResponse(exception, HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    private ErrorResponse buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        return new ErrorResponse(httpStatus.value(), exception.getMessage(), LocalDateTime.now());
    }
}
