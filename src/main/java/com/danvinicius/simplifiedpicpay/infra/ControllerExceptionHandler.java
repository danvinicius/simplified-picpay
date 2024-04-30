package com.danvinicius.simplifiedpicpay.infra;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.danvinicius.simplifiedpicpay.dto.ExceptionDTO;
import com.danvinicius.simplifiedpicpay.exceptions.NotificationException;
import com.danvinicius.simplifiedpicpay.exceptions.UnauthorizedTransactionException;
import com.danvinicius.simplifiedpicpay.exceptions.UserNotFoundException;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDTO> duplicateEntryHandler(DataIntegrityViolationException e) {
        Integer status = HttpStatus.BAD_REQUEST.value();
        ExceptionDTO exception = new ExceptionDTO("User already exists", status) ;
        return ResponseEntity.status(status).body(exception);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ExceptionDTO> duplicateEntryHandler(UserNotFoundException e) {
        Integer status = HttpStatus.NOT_FOUND.value();
        ExceptionDTO exception = new ExceptionDTO("User not found", status) ;
        return ResponseEntity.status(status).body(exception);
    }

    @ExceptionHandler(UnauthorizedTransactionException.class)
    public ResponseEntity<ExceptionDTO> unauthorizedTransactionHandler(UnauthorizedTransactionException e) {
        Integer status = HttpStatus.UNAUTHORIZED.value();
        ExceptionDTO exception = new ExceptionDTO(e.getMessage(), status) ;
        return ResponseEntity.status(status).body(exception);
    }

    @ExceptionHandler(NotificationException.class)
    public ResponseEntity<ExceptionDTO> notificationExceptionHandler(NotificationException e) {
        Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ExceptionDTO exception = new ExceptionDTO(e.getMessage(), status) ;
        return ResponseEntity.status(status).body(exception);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDTO> RuntimeExceptionHandler(NotificationException e) {
        Integer status = HttpStatus.INTERNAL_SERVER_ERROR.value();
        ExceptionDTO exception = new ExceptionDTO("Something went wrong", status) ;
        return ResponseEntity.status(status).body(exception);
    }

}
