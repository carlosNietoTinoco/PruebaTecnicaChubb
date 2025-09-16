package com.chubbTest.customer.domain.exception;

public class InvalidCustomerDataException extends RuntimeException {

    public InvalidCustomerDataException(String message) {
        super(message);
    }

    public InvalidCustomerDataException(String message, Throwable cause) {
        super(message, cause);
    }
}