package com.bobrov.meetup.service.exception;

public class FilterValidationException extends RuntimeException{
    public FilterValidationException() {
        super();
    }

    public FilterValidationException(String message) {
        super(message);
    }

    public FilterValidationException(String message, Throwable cause) {
        super(message, cause);
    }

    public FilterValidationException(Throwable cause) {
        super(cause);
    }
}
