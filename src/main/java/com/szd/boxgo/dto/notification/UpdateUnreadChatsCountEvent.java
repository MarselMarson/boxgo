package com.szd.boxgo.dto.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateUnreadChatsCountEvent extends ApplicationEvent {
    final Long userId;
    final Long chatId;

    public UpdateUnreadChatsCountEvent(Object source, Long userId, Long chatId) {
        super(source);
        this.userId = userId;
        this.chatId = chatId;
    }
}
