package com.szd.boxgo.websocket.exception;

public class MessageSerializationException extends RuntimeException {
    public MessageSerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
