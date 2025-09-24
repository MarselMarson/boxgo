package com.szd.boxgo.websocket.global;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"userId"})
public class UserData {
    Long userId;
    Set<Long> unreadChats;
    Long unreadChatsVersion;

    public long getUnreadChatsCount() {
        return this.unreadChats.size();
    }
}
