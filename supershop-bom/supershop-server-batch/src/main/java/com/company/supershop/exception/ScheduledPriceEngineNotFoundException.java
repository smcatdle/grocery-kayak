package com.company.supershop.exception;


public class ScheduledPriceEngineNotFoundException extends RuntimeException {
    public ScheduledPriceEngineNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public ScheduledPriceEngineNotFoundException(String message) {
        super(message);
    }

    public ScheduledPriceEngineNotFoundException() {
        super();
    }
}
