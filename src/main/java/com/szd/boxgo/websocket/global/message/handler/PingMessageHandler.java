package com.szd.boxgo.websocket.global.message.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.entity.chat.WebsocketMessageType;
import com.szd.boxgo.exception.IdAlreadyExistsException;
import com.szd.boxgo.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PingMessageHandler implements MessageHandler {
    private final ObjectMapper objectMapper;
    private final ChatService chatService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessagingService messagingService;

    @Override
    public boolean supports(TextMessage message) {
        try {
            var tree = objectMapper.readTree(message.getPayload());
            return tree.has("type")
                   && tree.get("type").asText().equals(WebsocketMessageType.PING.getTitle());
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void handle(WebSocketSession session, TextMessage message, Long senderId) {
        log.info("user: {} ping", senderId);
        try {
            messagingService.handlePing(senderId);
            //eventPublisher.publishEvent(new NewMessageEvent(this, senderId, savedMessage.getId()));
        } catch (IdAlreadyExistsException e) {
            log.warn(e.getMessage());
        }
    }
}
