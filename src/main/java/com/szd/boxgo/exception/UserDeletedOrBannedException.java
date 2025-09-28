package com.szd.boxgo.exception;

public class UserDeletedOrBannedException extends RuntimeException {

    public UserDeletedOrBannedException(String message) {
        super(message);
    }
}
