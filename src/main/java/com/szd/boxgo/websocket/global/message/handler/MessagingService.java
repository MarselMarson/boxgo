package com.szd.boxgo.websocket.global.message.handler;

import com.szd.boxgo.entity.chat.ChatMessage;
import org.springframework.web.socket.WebSocketSession;

public interface MessagingService {
    void handleSuccessConnection(WebSocketSession session);
    void handleFailureConnection(WebSocketSession session, int code);
    void handleIncomingMessage(ChatMessage message);
    void handleChatReadEvent(ChatMessage message);
    void confirmMessageDelivery(ChatMessage message);
    void handlePing(Long senderId);
    void updateUnreadChatsCount(Long userId, Long unreadChatsCount);
}
