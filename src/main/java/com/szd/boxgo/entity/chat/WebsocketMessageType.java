package com.szd.boxgo.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WebsocketMessageType {
    INCOMING_MESSAGE("chat.send"),
    OUTGOING_MESSAGE("chat.message"),
    AUTH("chat.auth"),
    UNREAD_TOTAL("chat.unreadTotal"),
    ERROR("chat.error"),
    READ_MESSAGE("chat.messageRead"),
    DELIVER_MESSAGE("chat.messageDelivered"),

    PING("ping"),
    PONG("pong");

    private final String title;
}
