package com.szd.boxgo.websocket.global.message.handler;

import com.szd.boxgo.dto.chat.PongDto;
import com.szd.boxgo.dto.chat.SendingMessageDto;
import com.szd.boxgo.dto.chat.UnreadChatsDto;
import com.szd.boxgo.dto.websocket.global.ConnectionDto;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.entity.chat.WebsocketMessageType;
import com.szd.boxgo.service.chat.message.ChatMessageService;
import com.szd.boxgo.websocket.global.TextMessageMapper;
import com.szd.boxgo.websocket.global.UserData;
import com.szd.boxgo.websocket.global.UserDataService;
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
    private final UserDataService userDataService;

    @Override
    public void handleSuccessConnection(WebSocketSession session, UserData userData) {
        ConnectionDto answer = ConnectionDto.builder()
                .type(WebsocketMessageType.AUTH.getTitle())
                .code(200)
                .unreadChatsTotal(userData.getUnreadChatsCount())
                .unreadChatsVersion(userDataService.incrementAndGetUnreadChatsCountVersion(userData.getUserId()))
                .build();

        sessionService.sendAuthMessage(session, textMessageMapper.toTextMessage(answer));
    }

    @Override
    public void handleFailureConnection(WebSocketSession session, int code, String message) {
        ConnectionDto answer = ConnectionDto.builder()
                .type(WebsocketMessageType.ERROR.getTitle())
                .code(code)
                .message(message)
                .build();

        sessionService.sendAuthMessage(session, textMessageMapper.toTextMessage(answer));
    }

    @Override
    public void handleIncomingMessage(ChatMessage message) {
        Long unreadChatsCount = userDataService.addUnreadChatAndGetCount(message.getRecipient().getId(), message.getChat().getId());
        Long unreadChatsVersion = userDataService.incrementAndGetUnreadChatsCountVersion(message.getRecipient().getId());

        sendMessage(message, message.getRecipient(), message.getSender(), unreadChatsCount, unreadChatsVersion);
        log.info("user {} sent msg {} to user {}",
                message.getSender().getId(),
                message.getId(),
                message.getRecipient().getId());

        returnMessage(message, message.getSender(), message.getRecipient(), 0L, 0L);
        log.info("msg {} to user {}",
                message.getId(),
                message.getSender().getId());
    }

    @Override
    public void handleChatReadEvent(ChatMessage message) {
        Long unreadChatsCount = userDataService.removeUnreadChatAndGetCount(message.getRecipient().getId(), message.getChat().getId());
        Long unreadChatsVersion = userDataService.incrementAndGetUnreadChatsCountVersion(message.getRecipient().getId());

        sendMessage(message, message.getSender(), message.getRecipient(), 0L, 0L);
        log.info("resending updated chat info with user {} to user {}",
                message.getSender().getId(),
                message.getRecipient().getId());

        returnMessage(message, message.getRecipient(), message.getSender(), unreadChatsCount, unreadChatsVersion);
        log.info("resending updated chat info with user {} to user {}",
                message.getRecipient().getId(),
                message.getSender().getId());
    }

    @Override
    public void handlePing(Long senderId, String ts) {
        TextMessage textMessage = textMessageMapper.toTextMessage(
                new PongDto(WebsocketMessageType.PONG.getTitle(), ts));
        sessionService.sendMessage(senderId, textMessage);
    }

    @Override
    public void confirmMessageDelivery(ChatMessage message) {
        /*Long unreadChatsCount = userDataService.addUnreadChatAndGetCount(message.getRecipient().getId(), message.getChat().getId());
        Long unreadChatsVersion = userDataService.incrementAndGetUnreadChatsCountVersion(message.getRecipient().getId());

        try {
            sendMessage(message, message.getRecipient());
            log.info("The message {} read by the recipient {} was returned to the recipient",
                    message.getId(),
                    message.getRecipient().getId());

            returnMessage(message, message.getSender());
            log.info("The message {} read by the recipient {} was returned to the sender {}",
                    message.getId(),
                    message.getRecipient().getId(),
                    message.getSender().getId());
        } catch (RuntimeException e) {
            log.warn(e.getMessage());
        }*/
    }

    public void sendMessage(ChatMessage message, User recipient, User companion, Long unreadChatsCount, Long unreadChatsVersion) {
        SendingMessageDto sendingMessageDto = messageService.getSendingMessageDto(
                message, companion, unreadChatsCount, unreadChatsVersion);
        TextMessage textMessage = textMessageMapper.toTextMessage(sendingMessageDto);
        sessionService.sendMessage(recipient.getId(), textMessage);
    }

    public void returnMessage(ChatMessage message, User sender, User companion, Long unreadChatsCount, Long unreadChatsVersion) {
        SendingMessageDto sendingMessageDto = messageService.getSendingMessageDto(
                message, companion, unreadChatsCount, unreadChatsVersion);
        TextMessage textMessage = textMessageMapper.toTextMessage(sendingMessageDto);
        sessionService.sendMessage(sender.getId(), textMessage);
    }

    public void updateUnreadChatsCount(Long userId, Long unreadChatsCount, Long unreadChatsCountVersion) {
        UnreadChatsDto notification = UnreadChatsDto.builder()
                .type(WebsocketMessageType.UNREAD_TOTAL.name())
                .unreadChatsTotal(unreadChatsCount)
                .unreadChatsVersion(unreadChatsCountVersion)
                .build();
        TextMessage textMessage = textMessageMapper.toTextMessage(notification);
        sessionService.sendMessage(userId, textMessage);
    }
}
