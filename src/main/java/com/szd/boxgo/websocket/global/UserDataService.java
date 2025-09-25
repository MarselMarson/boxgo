package com.szd.boxgo.websocket.global;

import com.szd.boxgo.service.chat.ChatRepoService;
import com.szd.boxgo.service.chat.UnreadChatsCountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class UserDataService {
    private final ConcurrentMap<Long, UserData> userData = new ConcurrentHashMap<>();

    private final ChatRepoService chatRepoService;
    private final UnreadChatsCountService unreadChatsCountService;

    @Transactional
    public UserData generate(Long userId) {
        if (!this.userData.containsKey(userId)) {
            UserData data = UserData.builder()
                    .userId(userId)
                    .unreadChats(getUnreadChatsId(userId))
                    .build();
            this.userData.put(userId, data);
            return data;
        }
        return this.userData.get(userId);
    }

    private Set<Long> getUnreadChatsId(Long userId) {
        return chatRepoService.findUnreadChatsIdByUserId(userId).stream()
                .collect(Collectors.toCollection(ConcurrentHashMap::newKeySet));
    }

    public Long incrementAndGetUnreadChatsCountVersion(Long userId) {
        return unreadChatsCountService.incrementAndGetUnreadChatsCountVersion(userId);
    }

    public void clear(Long userId) {
        this.userData.remove(userId);
    }

    /**
     *
     * @return if was removed return count, else return -1
     */
    public long removeUnreadChatAndGetCount(Long userId, Long chatId) {
        var data = userData.get(userId);
        if (data != null) {
            if (data.getUnreadChats().remove(chatId)) {
                return data.getUnreadChatsCount();
            }
        }
        return -1L;
    }

    /**
     *
     * @return if was added return count, else return -1
     */
    public long addUnreadChatAndGetCount(Long userId, Long chatId) {
        var data = userData.get(userId);
        if (data != null) {
            if (data.getUnreadChats().add(chatId)) {
                return data.getUnreadChatsCount();
            }
        }
        return -1L;
    }
}
