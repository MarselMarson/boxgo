package com.szd.boxgo.websocket.global.session;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

public interface SessionService {
    void terminate(WebSocketSession session);
    void terminate(Long userId);
    void sendMessage(Long userId, TextMessage message);
    void sendAuthMessage(WebSocketSession session, TextMessage message);
}
