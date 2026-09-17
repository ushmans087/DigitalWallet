package com.sample.payment_server.ExceptionClass;

public class SelfTransferException extends RuntimeException {

    public SelfTransferException(String message) {
        super(message);
    }
}