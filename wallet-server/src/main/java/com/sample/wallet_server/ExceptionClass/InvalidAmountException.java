package com.sample.wallet_server.ExceptionClass;

public class InvalidAmountException extends RuntimeException {

    public InvalidAmountException(String message) {
        super(message);
    }
}