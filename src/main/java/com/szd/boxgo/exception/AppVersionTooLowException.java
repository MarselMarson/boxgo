package com.szd.boxgo.exception;

public class AppVersionTooLowException extends RuntimeException {

    public AppVersionTooLowException(String message) {
        super(message);
    }
}
