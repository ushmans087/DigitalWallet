package com.sample.wallet_server.ExceptionClass;

public class AccountMismatchException extends RuntimeException {

    public AccountMismatchException(String message) {
        super(message);
    }
}