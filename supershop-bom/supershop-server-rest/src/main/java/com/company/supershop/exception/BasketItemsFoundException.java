package com.company.supershop.exception;


public class BasketItemsFoundException extends RuntimeException {
    public BasketItemsFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public BasketItemsFoundException(String message) {
        super(message);
    }

    public BasketItemsFoundException() {
        super();
    }
}
