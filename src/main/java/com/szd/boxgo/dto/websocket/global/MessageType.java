package com.szd.boxgo.dto.websocket.global;

public enum MessageType {
    AUTH("chat.auth"), ERROR("chat.error");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
