package com.szd.boxgo.repo.chat;

import com.szd.boxgo.entity.chat.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {
    boolean existsBySenderIdAndFrontendId(Long senderId, Long frontendId);

    @Modifying
    @Query("""
        UPDATE ChatMessage cm
        SET cm.status = 'READ',
            cm.updatedAt = CURRENT_TIMESTAMP
        WHERE cm.chat.id = :chatId
               AND cm.recipient.id = :chatPartnerId
               AND cm.id <= :lastMessageId
               AND cm.status = 'SENT'
    """)
    void setMessagesStatusToRead(Long chatId, Long chatPartnerId, Long lastMessageId);
}
