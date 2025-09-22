package com.szd.boxgo.websocket;

import com.szd.boxgo.websocket.global.session.SessionRepository;
import com.szd.boxgo.websocket.global.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.PongMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
abstract public class MyWebSocketHandler extends TextWebSocketHandler {
    protected final SessionRepository sessionRepository;
    protected final SessionService sessionService;

    protected MyWebSocketHandler(SessionRepository sessionRepository, SessionService sessionService) {
        this.sessionRepository = sessionRepository;
        this.sessionService = sessionService;
    }

    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        super.handlePongMessage(session, message);
    }
}