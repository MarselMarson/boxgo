package com.szd.boxgo.dto.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatReadEvent extends ApplicationEvent {
    final Long readerId;
    final Long chatPartnerId;
    final Long chatId;

    public ChatReadEvent(Object source, Long readerId, Long chatPartnerId, Long chatId) {
        super(source);
        this.readerId = readerId;
        this.chatPartnerId = chatPartnerId;
        this.chatId = chatId;
    }
}
