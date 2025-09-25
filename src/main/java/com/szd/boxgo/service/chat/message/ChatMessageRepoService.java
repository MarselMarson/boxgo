package com.szd.boxgo.service.chat.message;

import com.szd.boxgo.dto.chat.ChatMessageDto;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.repo.chat.ChatMessageRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageRepoService {
    private final ChatMessageRepo messageRepo;

    public ChatMessage saveMessage(ChatMessage message) {
        return messageRepo.save(message);
    }

    public ChatMessage getMessageById(Long id) {
        return messageRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("message with id: " + id + " not found"));
    }

    @Transactional
    public void setMessagesStatusToRead(Long chatId, Long userId) {
        messageRepo.setMessagesStatusToRead(chatId, userId);
    }

    @Transactional
    public void setMessagesStatusToRead(Long chatId, Long userId, Long lastMessageId) {
        messageRepo.setMessagesStatusToRead(chatId, userId, lastMessageId);
    }

    public Page<ChatMessageDto> getMessagesByChatId(Long chatId, Pageable pageable) {
        return messageRepo.findAllByChatId(chatId, pageable);
    }

    @Transactional
    public Integer findCountOfUnreadMessages(Long chatId, Long chatPartnerId) {
        return messageRepo.findCountOfUnreadMessages(chatId, chatPartnerId);
    }
}
