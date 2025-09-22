package com.szd.boxgo.service.chat;

import com.szd.boxgo.dto.chat.ChatMessageDto;
import com.szd.boxgo.dto.chat.IncomingMessageDto;
import com.szd.boxgo.entity.chat.Chat;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.service.chat.message.ChatMessageRepoService;
import com.szd.boxgo.service.chat.message.ChatMessageService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatService {
    private final ChatMessageService messageService;
    private final ChatRepoService chatRepoService;
    private final ChatMessageRepoService messageRepoService;

    private final ApplicationEventPublisher eventPublisher;

    @PersistenceContext
    private final EntityManager entityManager;

    public ChatMessage createMessage(IncomingMessageDto newMessageDto, Long senderId) {
        //TODO cache
        Chat chat = chatRepoService.getChatIdOrCreate(
                newMessageDto.getInterlocutorId(),
                newMessageDto.getSegmentId());

        return messageService.saveMessage(newMessageDto, chat, senderId);
    }

    @Transactional
    public Page<ChatMessageDto> openChat(Long userId, Long chatPartnerId, Pageable pageable) {
        /*Long chatId = chatRepoService.getChatIdOrCreate(userId, chatPartnerId);

        messageRepoService.setMessagesStatusToRead(chatId, chatPartnerId);
        Page<ChatMessageDto> messages = messageRepoService.getMessagesByChatId(chatId, chatPartnerId, pageable);

        eventPublisher.publishEvent(new ChatReadEvent(this, userId, chatPartnerId, chatId));*/

        return null;
    }

    public Chat getChatById(Long id) {
        return chatRepoService.getChatById(id);
    }
}
