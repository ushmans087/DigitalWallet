package com.sample.wallet_server.ExceptionClass;

public class BankAccountNotFoundException extends RuntimeException {

    public BankAccountNotFoundException(String message) {
        super(message);
    }
}