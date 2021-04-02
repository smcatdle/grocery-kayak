package com.company.supershop.exception;


public class NoBasketsFoundException extends RuntimeException {
    public NoBasketsFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public NoBasketsFoundException(String message) {
        super(message);
    }

    public NoBasketsFoundException() {
        super();
    }
}
