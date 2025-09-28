package com.szd.boxgo.websocket.global.session;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Component
@Slf4j
class GlobalSessionRepository implements SessionRepository {
    private final ConcurrentMap<Long, Set<WebSocketSession>> userSessions = new ConcurrentHashMap<>();
    private final ConcurrentMap<WebSocketSession, Long> sessionUser = new ConcurrentHashMap<>();

    @Override
    public void register(Long userId, WebSocketSession session) {
        if (sessionUser.containsKey(session) && sessionUser.get(session).equals(userId)) {
            log.warn("Session {} is already registered for user {}", session.getId(), userId);
            return;
        }

        userSessions
                .computeIfAbsent(userId, k -> ConcurrentHashMap.newKeySet())
                .add(session);
        sessionUser.put(session, userId);

        log.info("Registering session {} for user {}", session.getId(), userId);
    }

    @Override
    public Optional<Long> getUserId(WebSocketSession session) {
        return Optional.ofNullable(sessionUser.get(session));
    }

    @Override
    public Set<WebSocketSession> getSessions(Long userId) {
        var sessions = userSessions.get(userId);
        return sessions != null
                ? sessions
                : Collections.emptySet();
    }

    @Override
    public void remove(WebSocketSession session) {
        Long userId = sessionUser.remove(session);
        if (userId != null) {
            userSessions.computeIfPresent(userId, (key, sessions) -> {
                sessions.remove(session);
                return sessions.isEmpty() ? null : sessions;
            });
        }

        log.info("Removing session {} for user {}", session.getId(), userId);
    }

    @Override
    public Set<WebSocketSession> removeUserSessions(Long userId) {
        log.info("Removing all sessions user {}", userId);

        var sessions = userSessions.remove(userId);
        if (sessions != null) {
            sessions.forEach(sessionUser::remove);
        }
        return sessions != null
                ? sessions
                : Collections.emptySet();
    }

    @Override
    public boolean isContainSession(WebSocketSession session) {
        return sessionUser.containsKey(session);
    }
}
