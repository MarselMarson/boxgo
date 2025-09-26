package com.szd.boxgo.service.chat;

import com.szd.boxgo.repo.chat.UnreadChatsCountVersionRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UnreadChatsCountService {
    private final UnreadChatsCountVersionRepo repo;

    @Transactional
    public int incrementAndGetUnreadChatsCountVersion(Long userId) {
        return repo.incrementAndGetVersion(userId);
    }
}
