package com.nextuple.Inventory.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
@RestControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(UserNotFoundException.class)
    public final ResponseEntity<Object> handleUserNotFoundException(Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SupplyAndDemandExistException.class)
    public final ResponseEntity<Object> handleSupplyAndDemandNotFoundException(Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

}
