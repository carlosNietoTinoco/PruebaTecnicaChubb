package com.chubbTest.customer.domain.exception;

public class CustomerStatusConflictException extends RuntimeException {

    public CustomerStatusConflictException(String message) {
        super(message);
    }

    public CustomerStatusConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}