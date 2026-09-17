package com.sample.payment_server.ExceptionClass;

public class InvalidAccountsException extends RuntimeException {

    public InvalidAccountsException(String message) {
        super(message);
    }
}