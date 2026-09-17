package com.sample.wallet_server.ExceptionClass;

public class NotEnoughDetailsException extends RuntimeException {

    public NotEnoughDetailsException(String message) {
        super(message);
    }
}