package com.szd.boxgo.repo.chat;

import com.szd.boxgo.dto.chat.ChatMessageDto;
import com.szd.boxgo.entity.chat.ChatMessage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatMessageRepo extends JpaRepository<ChatMessage, Long>, JpaSpecificationExecutor<ChatMessage> {
    @Modifying
    @Query("""
        UPDATE ChatMessage cm
        SET cm.status = 'READ',
            cm.updatedAt = CURRENT_TIMESTAMP
        WHERE cm.chat.id = :chatId
                AND cm.status = 'SENT'
                AND cm.recipient.id = :recipientId
    """)
    void setMessagesStatusToRead(Long chatId, Long recipientId);

    @Modifying
    @Query("""
        UPDATE ChatMessage cm
        SET cm.status = 'READ',
            cm.updatedAt = CURRENT_TIMESTAMP
        WHERE cm.chat.id = :chatId
               AND cm.recipient.id = :recipientId
               AND cm.id <= :lastMessageId
               AND cm.status = 'SENT'
    """)
    void setMessagesStatusToRead(Long chatId, Long recipientId, Long lastMessageId);

    @Query("""
        SELECT new com.szd.boxgo.dto.chat.ChatMessageDto
            (cm.id,
            cm.frontendId,
            cm.sender.id,
            cm.content,
            cm.status,
            cm.version,
            cm.createdAt)
        FROM ChatMessage cm
        WHERE cm.chat.id = :chatId
        ORDER BY cm.id desc
    """)
    Page<ChatMessageDto> findAllByChatId(@Param("chatId") Long chatId, Pageable pageable);

    @Query("""
        SELECT count(cm) FROM ChatMessage cm
        WHERE cm.chat.id = :chatId AND cm.status = 'SENT' AND cm.sender.id = :chatPartnerId
    """)
    Integer findCountOfUnreadMessages(Long chatId, Long chatPartnerId);
}
