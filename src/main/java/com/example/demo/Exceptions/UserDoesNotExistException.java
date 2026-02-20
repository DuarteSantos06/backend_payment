package com.example.demo.Exceptions;

public class UserDoesNotExistException extends RuntimeException{

    public UserDoesNotExistException(String message) {
        super(message);
    }


}
