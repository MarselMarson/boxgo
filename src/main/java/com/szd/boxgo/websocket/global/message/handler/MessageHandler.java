package com.szd.boxgo.websocket.global.message.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessageHandler {
    boolean supports(TextMessage message);
    void handle(WebSocketSession session, TextMessage message, Long senderId);
}