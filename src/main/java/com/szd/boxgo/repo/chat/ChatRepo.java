package com.szd.boxgo.repo.chat;

import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.chat.Chat;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.Set;

@Repository
public interface ChatRepo extends JpaRepository<Chat, Long> {
    Optional<Chat> findByUserIdAndSegmentId(Long userId, Long segmentId);

    Page<Chat> findByListingOwnerIdOrUserIdOrderByLastMessageCreatedAtDesc(Long ownerId, Long userId, Pageable pageable);

    @Modifying
    @Query(
            """
                UPDATE Chat c SET c.lastMessage.id = :lastMessageId,
                c.lastMessageCreatedAt = :lastMessageCreatedAt,
                c.lastMessageContent = :lastMessageContent,
                c.lastMessageSender = :lastMessageSender
                WHERE c.id = :chatId
                    AND (c.lastMessage.id IS NULL
                        OR c.lastMessage.id < :lastMessageId)
            """)
    void updateLastMessage(@Param("chatId") Long chatId,
                           @Param("lastMessageId") Long lastMessageId,
                           @Param("lastMessageCreatedAt") OffsetDateTime lastMessageCreatedAt,
                           @Param("lastMessageContent") String lastMessageContent,
                           @Param("lastMessageSender") User lastMessageSender
    );

    @Query(
            """
                SELECT c.id FROM Chat c
                WHERE (c.listingOwner.id = :userId OR c.user.id = :userId)
                    AND c.lastMessage IS NOT NULL
                    AND c.lastMessageSender.id <> :userId
                    AND c.lastMessage.status = 'SENT'
            """)
    Set<Long> findAllUnreadChatsIdByUserId(@Param("userId") Long userId);
}
