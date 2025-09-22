package com.szd.boxgo.repo.chat;

import com.szd.boxgo.entity.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUserIdAndSegmentId(Long userId, Long segmentId);
}
