package com.szd.boxgo.eventListener.chat;

import com.szd.boxgo.dto.notification.ChatReadEvent;
import com.szd.boxgo.entity.chat.Chat;
import com.szd.boxgo.service.chat.ChatService;
import com.szd.boxgo.websocket.global.UserDataService;
import com.szd.boxgo.websocket.global.message.handler.MessagingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class ChatReadEventGlobalWebSocketHandler {
    private final ChatService chatService;
    private final MessagingService messagingService;
    private final UserDataService userDataService;

    @TransactionalEventListener
    public void handleMessageReading(ChatReadEvent event) {
        Chat chat = chatService.getChatById(event.getChatId());
        if (chat.getLastMessage() != null) {
            messagingService.handleChatReadEvent(chat.getLastMessage());
        }

        long unreadChatsCount = userDataService
                .removeUnreadChatAndGetCount(event.getReaderId(), event.getChatId());
        if (unreadChatsCount >= 0) {
            long unreadChatsCountVersion = userDataService
                    .incrementAndGetUnreadChatsCountVersion(event.getReaderId());
            messagingService.updateUnreadChatsCount(event.getReaderId(), unreadChatsCount, unreadChatsCountVersion);
        }
    }
}
