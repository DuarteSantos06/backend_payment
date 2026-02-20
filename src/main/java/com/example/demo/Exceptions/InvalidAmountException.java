package com.example.demo.Exceptions;

public class InvalidAmountException extends RuntimeException{

    public InvalidAmountException(String message) {
        super(message);
    }
}
