package com.progect.BankingApp.Exception;

public class ConflictException extends RuntimeException{
    public ConflictException() {
        super();
    }

    public ConflictException(String message) {
        super(message);
    }
}
