package com.szd.boxgo.service.chat;

import com.szd.boxgo.repo.chat.UnreadChatsCountVersionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnreadChatsCountService {
    private final UnreadChatsCountVersionRepo repo;

    public int incrementAndGetUnreadChatsCountVersion(Long userId) {
        repo.incrementVersion(userId);
        return repo.findVersionByUserId(userId);
    }
}
