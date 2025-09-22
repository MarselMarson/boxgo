package com.szd.boxgo.websocket.global.session;

import org.springframework.web.socket.WebSocketSession;

import java.util.Optional;
import java.util.Set;

public interface SessionRepository {
    void register(Long userId, WebSocketSession session);
    Optional<Long> getUserId(WebSocketSession session);
    Set<WebSocketSession> getSessions(Long userId);
    void remove(WebSocketSession session);
    Set<WebSocketSession> removeUserSessions(Long userId);
    boolean isContainSession(WebSocketSession session);
}
