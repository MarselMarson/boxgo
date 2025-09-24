package com.szd.boxgo.dto.notification;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEvent;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewMessageEvent extends ApplicationEvent {
    final Long senderId;
    final Long messageId;

    public NewMessageEvent(Object source, Long senderId, Long messageId) {
        super(source);
        this.senderId = senderId;
        this.messageId = messageId;
    }
}
