package com.nextuple.Inventory.management.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends RuntimeException {


    public UserNotFoundException(String message) {
        super(message);
    }


}
