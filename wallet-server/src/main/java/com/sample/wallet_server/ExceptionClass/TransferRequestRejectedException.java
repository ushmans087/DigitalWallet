package com.sample.wallet_server.ExceptionClass;

public class TransferRequestRejectedException extends RuntimeException {

    public TransferRequestRejectedException(String message) {
        super(message);
    }
}