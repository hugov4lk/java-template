package com.example.java.template.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String message, Exception exception) {
        super(message, exception);
    }

    public NotFoundException(String message) {
        super(message);
    }
}