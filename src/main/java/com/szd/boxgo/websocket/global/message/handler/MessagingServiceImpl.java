package com.szd.boxgo.websocket.global.message.handler;

import com.szd.boxgo.dto.chat.PongDto;
import com.szd.boxgo.dto.chat.SendingMessageDto;
import com.szd.boxgo.dto.websocket.global.ConnectionDto;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.entity.chat.WebsocketMessageType;
import com.szd.boxgo.service.chat.message.ChatMessageService;
import com.szd.boxgo.websocket.global.TextMessageMapper;
import com.szd.boxgo.websocket.global.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Service
@RequiredArgsConstructor
public class MessagingServiceImpl implements MessagingService {
    private final ChatMessageService messageService;
    private final TextMessageMapper textMessageMapper;
    private final SessionService sessionService;

    @Override
    public void handleSuccessConnection(WebSocketSession session) {
        ConnectionDto answer = ConnectionDto.builder()
                .type(WebsocketMessageType.AUTH.getTitle())
                .code(200)
                .build();

        sessionService.sendAuthMessage(session, textMessageMapper.toTextMessage(answer));
    }

    @Override
    public void handleFailureConnection(WebSocketSession session, int code) {
        ConnectionDto answer = ConnectionDto.builder()
                .type(WebsocketMessageType.ERROR.getTitle())
                .code(code)
                .build();

        sessionService.sendAuthMessage(session, textMessageMapper.toTextMessage(answer));
    }

    @Override
    public void handleIncomingMessage(ChatMessage message) {
        sendMessage(message);
        log.info("user {} sent msg {} to user {}",
                message.getSender().getId(),
                message.getId(),
                message.getRecipient().getId());

        returnMessage(message);
        log.info("msg {} to user {}",
                message.getId(),
                message.getSender().getId());
    }

    @Override
    public void handleChatReadEvent(ChatMessage message) {
        sendMessage(message);
        log.info("resending updated chat info with user {} to user {}",
                message.getSender().getId(),
                message.getRecipient().getId());

        returnMessage(message);
        log.info("resending updated chat info with user {} to user {}",
                message.getRecipient().getId(),
                message.getSender().getId());
    }

    @Override
    public void handlePing(Long senderId) {
        TextMessage textMessage = textMessageMapper.toTextMessage(new PongDto(WebsocketMessageType.PING.getTitle()));
        sessionService.sendMessage(senderId, textMessage);
    }

    @Override
    public void confirmMessageDelivery(ChatMessage message) {
        try {
            sendMessage(message);
            log.info("The message {} read by the recipient {} was returned to the recipient",
                    message.getId(),
                    message.getRecipient().getId());
            returnMessage(message);

            log.info("The message {} read by the recipient {} was returned to the sender {}",
                    message.getId(),
                    message.getRecipient().getId(),
                    message.getSender().getId());
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }
    }

    public void sendMessage(ChatMessage message) {
        SendingMessageDto sendingMessageDto = messageService.getSendingMessageDto(message, message.getRecipient());
        TextMessage textMessage = textMessageMapper.toTextMessage(sendingMessageDto);
        sessionService.sendMessage(message.getRecipient().getId(), textMessage);
    }

    public void returnMessage(ChatMessage message) {
        SendingMessageDto sendingMessageDto = messageService.getSendingMessageDto(message, message.getSender());
        TextMessage textMessage = textMessageMapper.toTextMessage(sendingMessageDto);
        sessionService.sendMessage(message.getSender().getId(), textMessage);
    }

    public void updateUnreadChatsCount(Long userId, Long unreadChatsCount) {
        /*GlobalWebSocketNotificationDto notification = GlobalWebSocketNotificationDto.builder()
                .type(MessageType.chat.name())
                .countChats(unreadChatsCount)
                .build();
        TextMessage textMessage = textMessageMapper.toTextMessage(notification);
        sessionService.sendMessage(userId, textMessage);*/
    }
}
