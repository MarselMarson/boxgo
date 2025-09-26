package com.szd.boxgo.websocket.global.message.handler;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.dto.chat.ReadMessageDto;
import com.szd.boxgo.dto.notification.UpdateUnreadChatsCountEvent;
import com.szd.boxgo.entity.chat.Chat;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.entity.chat.WebsocketMessageType;
import com.szd.boxgo.service.chat.ChatRepoService;
import com.szd.boxgo.service.chat.message.ChatMessageService;
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
public class ReadMessageHandler implements MessageHandler {
    private final ObjectMapper objectMapper;
    private final ChatMessageService messageService;
    private final ChatRepoService chatRepoService;
    private final ApplicationEventPublisher eventPublisher;
    private final MessagingService messagingService;

    @Override
    public boolean supports(TextMessage message) {
        try {
            var tree = objectMapper.readTree(message.getPayload());
            return tree.has("type")
                   && tree.get("type").asText().equals(WebsocketMessageType.READ_MESSAGE.getTitle())
                   && tree.has("listingId")
                   && tree.has("segmentId")
                   && tree.has("clientId")
                   && tree.has("messageId");
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    @Transactional
    public void handle(WebSocketSession session, TextMessage message, Long senderId) {
        try {
            ReadMessageDto dto = objectMapper.readValue(message.getPayload(), ReadMessageDto.class);

            log.info("user: {} read msg {}", senderId, dto.getMessageId());

            Chat chat = chatRepoService.getChatIdOrCreate(dto.getInterlocutorId(), dto.getSegmentId());
            ChatMessage readMessage = messageService
                    .setMessageStatusToRead(dto.getMessageId(), senderId, chat.getId());
            messagingService.handleChatReadEvent(readMessage);

            eventPublisher.publishEvent(new UpdateUnreadChatsCountEvent(this, senderId, chat.getId()));
        } catch (JacksonException e) {
            log.warn("Can't map message from user {} session {}: {}", senderId, session, e.getMessage());
        }
    }
}
