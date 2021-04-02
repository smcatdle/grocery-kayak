package com.company.supershop.exception;


public class ChainNotFoundException extends RuntimeException {
    public ChainNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ChainNotFoundException(String message) {
        super(message);
    }

    public ChainNotFoundException() {
        super();
    }
}
