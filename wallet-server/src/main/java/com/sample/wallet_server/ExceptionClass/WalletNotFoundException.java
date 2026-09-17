package com.sample.wallet_server.ExceptionClass;

public class WalletNotFoundException extends RuntimeException {

    public WalletNotFoundException(String message) {
        super(message);
    }
}