package com.sample.test.ExceptionClass;

public class AccountAlreadyExistException extends RuntimeException{
    public AccountAlreadyExistException(String message){
        super(message);
    }
}
