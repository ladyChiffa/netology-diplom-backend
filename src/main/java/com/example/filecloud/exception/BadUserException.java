package com.example.filecloud.exception;

public class BadUserException extends RuntimeException {
    public BadUserException(String msg) {
        super(msg);
    }
}
