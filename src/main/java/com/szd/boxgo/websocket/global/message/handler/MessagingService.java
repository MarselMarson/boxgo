package com.szd.boxgo.websocket.global.message.handler;

import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.websocket.global.UserData;
import org.springframework.web.socket.WebSocketSession;

public interface MessagingService {
    void handleSuccessConnection(WebSocketSession session, UserData userData);
    void handleFailureConnection(WebSocketSession session, int code, String message);
    void handleIncomingMessage(ChatMessage message);
    void handleChatReadEvent(ChatMessage message);
    void confirmMessageDelivery(ChatMessage message);
    void handlePing(Long senderId, String ts);
    void updateUnreadChatsCount(Long userId, Long unreadChatsCount);
}
