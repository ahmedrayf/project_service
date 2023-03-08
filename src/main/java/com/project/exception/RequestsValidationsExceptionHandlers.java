package com.project.exception;

import com.project.dto.response.AppResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class RequestsValidationsExceptionHandlers extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info("MethodArgumentNotValidException: " + ex.getMessage());
        List<String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.toList());

        return new ResponseEntity<>(AppResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .validationErrors(errors)
                .message("Invalid inputs")
                .timestamp(LocalDateTime.now())
                .build()
                , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> constraintViolationException(ConstraintViolationException ex, WebRequest request) {
        log.info("ConstraintViolationException: " + ex.getMessage());
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(cv -> errors.add(cv.getMessage()));
        return new ResponseEntity<>(AppResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .validationErrors(errors)
                .message("Invalid inputs")
                .timestamp(LocalDateTime.now())
                .build()
                , HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        log.info("MethodArgumentTypeMismatchException: " + ex.getMessage());
        return new ResponseEntity<>(AppResponse.builder()
                .httpStatus(HttpStatus.BAD_REQUEST)
                .validationErrors(ex.getMessage())
                .message("Invalid inputs")
                .timestamp(LocalDateTime.now())
                .build()
                , HttpStatus.BAD_REQUEST
        );
    }

}
