package com.company.supershop.exception;


public class ShelfNotFoundException extends RuntimeException {
    public ShelfNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ShelfNotFoundException(String message) {
        super(message);
    }

    public ShelfNotFoundException() {
        super();
    }
}
