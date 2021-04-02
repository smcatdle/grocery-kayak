package com.company.supershop.exception;


public class LogoutException extends RuntimeException {
    public LogoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public LogoutException(String message) {
        super(message);
    }

    public LogoutException() {
        super();
    }
}
