package com.szd.boxgo.repo.chat;

import com.szd.boxgo.entity.chat.UnreadChatsCountVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnreadChatsCountVersionRepo extends JpaRepository<UnreadChatsCountVersion, Long> {
    UnreadChatsCountVersion findByUserId(Long userId);
}
