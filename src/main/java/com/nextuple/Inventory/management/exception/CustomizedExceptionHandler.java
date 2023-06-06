package com.nextuple.Inventory.management.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
@RestControllerAdvice
public class CustomizedExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(OrganizationNotFoundException.class)
    public final ResponseEntity<Error> handleOrganizationNotFoundException(Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SupplyAndDemandExistException.class)
    public final ResponseEntity<Error> handleSupplyAndDemandNotFoundException(Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ItemNotFoundException.class)
    public final ResponseEntity<Error> ItemNotFoundException(Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(DemandNotFoundException.class)
    public final ResponseEntity<Error> DemandNotFoundException(Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(SupplyNotFoundException.class)
    public final ResponseEntity<Error>SupplyNotFoundException (Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(ThresholdNotFoundException.class)
    public final ResponseEntity<Error>ThresholdNotFoundException (Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(LocationNotFoundException.class)
    public final ResponseEntity<Error>LocationNotFoundException (Exception ex) throws Exception{
        ErrorDetails errorDetails = new ErrorDetails(LocalDateTime.now(),ex.getMessage());
        return new ResponseEntity(errorDetails, HttpStatus.NOT_FOUND);
    }


}
