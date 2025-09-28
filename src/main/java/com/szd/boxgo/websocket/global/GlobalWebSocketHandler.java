package com.szd.boxgo.websocket.global;

import com.szd.boxgo.entity.chat.WebsocketErrorCode;
import com.szd.boxgo.exception.InvalidTokenException;
import com.szd.boxgo.exception.TokenIsMissingException;
import com.szd.boxgo.exception.UserDeletedOrBannedException;
import com.szd.boxgo.i18n.AppLocale;
import com.szd.boxgo.i18n.AppMessages;
import com.szd.boxgo.security.AppVersionAndLocaleFilter;
import com.szd.boxgo.websocket.MyWebSocketHandler;
import com.szd.boxgo.websocket.global.auth.WebSocketAuthenticator;
import com.szd.boxgo.websocket.global.message.handler.MessageHandler;
import com.szd.boxgo.websocket.global.message.handler.MessagingService;
import com.szd.boxgo.websocket.global.session.SessionRepository;
import com.szd.boxgo.websocket.global.session.SessionService;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.util.List;
import java.util.Map;

@Component
@Slf4j
public class GlobalWebSocketHandler extends MyWebSocketHandler {
    private final List<MessageHandler> messageHandlers;
    private final WebSocketAuthenticator authenticator;
    private final MessagingService messagingService;
    private final UserDataService userDataService;
    private final AppVersionAndLocaleFilter versionFilter;

    public GlobalWebSocketHandler(List<MessageHandler> messageHandlers,
                                  SessionRepository sessionRepository,
                                  SessionService sessionService,
                                  WebSocketAuthenticator authenticator,
                                  MessagingService messagingService, UserDataService userDataService, AppVersionAndLocaleFilter versionFilter) {
        super(sessionRepository, sessionService);
        this.messageHandlers = messageHandlers;
        this.authenticator = authenticator;
        this.messagingService = messagingService;
        this.userDataService = userDataService;
        this.versionFilter = versionFilter;
    }

    @Override
    protected void handleTextMessage(@NonNull WebSocketSession session, @NotNull TextMessage message) {
        String payload = message.getPayload();
        log.info("Received WebSocket message - Session: {}, Payload: {}", session.getId(), payload);

        if (isAuthenticated(session)) {
            Long authenticatedUserId = sessionRepository.getUserId(session).orElse(null);
            if (authenticatedUserId == null) {
                sessionService.terminate(session);
            } else {
                handleMessage(session, message, authenticatedUserId);
            }
        }
    }

    private void handleSuccessfulAuth(Long userId, WebSocketSession session) {
        sessionRepository.register(userId, session);
        UserData userData = userDataService.generate(userId);
        messagingService.handleSuccessConnection(session, userData);
        log.info("User {} connected to global websocket", userId);
    }

    private void handleFailedAuth(WebSocketSession session, int code, String message) {
        log.warn("Authentication to global websocket failed, session: {}", session.getId());
        messagingService.handleFailureConnection(session, code, message);

        sessionService.terminate(session);
    }

    @Override
    public void afterConnectionEstablished(@NotNull WebSocketSession session) {
        Map<String, Object> attributes = session.getAttributes();
        String authHeader = (String) attributes.get("Authorization");
        String lang = (String) attributes.get(AppLocale.ATTR_LANG);
        String appVersion = (String) attributes.get(AppVersionAndLocaleFilter.HEADER_APP_VERSION);

        if (!versionFilter.checkVersion(appVersion)) {
            handleFailedAuth(session, WebsocketErrorCode.APP_VERSION_DEPRECATED.getCode(),
                    AppMessages.get(lang, AppMessages.KEY_APP_VERSION_EXPIRED));
            return;
        }

        try {
            handleSuccessfulAuth(authenticator.authenticate(authHeader).getId(), session);
        } catch (TokenIsMissingException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            handleFailedAuth(session, WebsocketErrorCode.TOKEN_INVALID.getCode(),
                    AppMessages.get(lang, AppMessages.KEY_TOKEN_IS_MISSING));
        } catch (InvalidTokenException e) {
            log.warn("Authentication failed: {}", e.getMessage());
            handleFailedAuth(session, WebsocketErrorCode.TOKEN_INVALID.getCode(),
                    AppMessages.get(lang, AppMessages.KEY_TOKEN_EXPIRED));
        } catch (UserDeletedOrBannedException e) {
            log.warn("User auth failed: {}", e.getMessage());
            handleFailedAuth(session, WebsocketErrorCode.USER_DELETED_OR_BANNED.getCode(),
                    AppMessages.get(lang, AppMessages.KEY_USER_BLOCKED));
        } catch (Exception e) {
            log.error("Error during WebSocket connection establishment: " + e.getMessage());
            handleFailedAuth(session, WebsocketErrorCode.KEY_GENERIC_ERROR.getCode(),
                    AppMessages.get(lang, AppMessages.KEY_GENERIC_ERROR));
        }
    }

    private boolean isAuthenticated(WebSocketSession session) {
        return sessionRepository.isContainSession(session);
    }

    private void handleMessage(WebSocketSession session, TextMessage message, Long senderId) {
        long appropriateHandlersCount = messageHandlers.stream()
                .filter(handler -> handler.supports(message))
                .peek(handler -> handler.handle(session, message, senderId))
                .count();

        if (appropriateHandlersCount == 0) {
            handleUnsupportedMessage(session, message);
        }
    }

    public void handleUnsupportedMessage(WebSocketSession session, TextMessage message) {
        log.warn("Unsupported message: {}, session: {}", message, session.getId());
        sessionService.terminate(session);
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        log.warn("Transport error: {}, session: {}", exception.getMessage(), session.getId());
        sessionService.terminate(session);
    }

    @Override
    public void afterConnectionClosed(@NonNull WebSocketSession session, @NonNull CloseStatus status) {
        sessionRepository.remove(session);
        log.info("Session: {} was closed with status: {}", session, status);
    }
}