package com.szd.boxgo.websocket.global.session;

import com.szd.boxgo.websocket.global.UserDataService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
public class SessionServiceImpl implements SessionService {
    private final SessionRepository sessionRepository;
    private final UserDataService userDataService;

    @Override
    public void terminate(WebSocketSession session) {
        try {
            if (session.isOpen()) {
                session.close();
                log.info("Session {} terminated", session.getId());
            }
        } catch (IOException e) {
            log.error("Error closing session {}: {}", session.getId(), e.getMessage());
        }

        Long userId = sessionRepository.getUserId(session).orElse(null);
        sessionRepository.remove(session);

        if (userId != null) {
            var sessions = sessionRepository.getSessions(userId);

            if (sessions.isEmpty()) {
                userDataService.clear(userId);
                log.info("The user {} no longer has any open sessions left. Data cleared", userId);
            }
        }
    }

    @Override
    public void terminate(Long userId) {
        var sessions = sessionRepository.removeUserSessions(userId);

        sessions.forEach(session -> {
            try {
                if (session.isOpen()) {
                    session.close();
                    log.info("Session {} closed for user {}", session.getId(), userId);
                }
            } catch (IOException e) {
                log.error("Error closing session {}: {}", session.getId(), e.getMessage());
            }
        });

        userDataService.clear(userId);
        log.info("All {} sessions terminated for user {}. Data cleared", sessions.size(), userId);
    }

    @Override
    public void sendMessage(Long userId, TextMessage message) {
        for (WebSocketSession session : sessionRepository.getSessions(userId)) {
            try {
                if (session.isOpen()) {
                    session.sendMessage(message);
                    log.debug("Message sent to session {} (user {})", session.getId(), userId);
                } else {
                    sessionRepository.remove(session);
                    log.warn("Closed session {} removed from repository", session.getId());
                }
            } catch (IOException e) {
                log.error("Failed to send message to user {} session {}: {}",
                        userId, session.getId(), e.getMessage());
            }
        }
    }

    @Override
    public void sendAuthMessage(WebSocketSession session, TextMessage message) {
        Long userId = sessionRepository.getUserId(session).orElse(null);
        try {
            if (session.isOpen()) {
                session.sendMessage(message);
                log.debug("Auth message sent to session {}", session.getId());
            } else {
                sessionRepository.remove(session);
                log.warn("Closed session {} removed from repository", session.getId());
            }
        } catch (IOException e) {
            log.error("Authentication error [User {} Session: {}]: {}",
                    userId, session.getId(), e.getMessage());
            terminate(session);
        }
    }
}
