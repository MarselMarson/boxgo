package com.szd.boxgo.service.chat;

import com.szd.boxgo.dto.chat.ChatDto;
import com.szd.boxgo.dto.chat.ChatInMessageDto;
import com.szd.boxgo.dto.chat.ChatMessageDto;
import com.szd.boxgo.dto.chat.IncomingMessageDto;
import com.szd.boxgo.dto.notification.ChatReadEvent;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.chat.Chat;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.mapper.ChatMapper;
import com.szd.boxgo.mapper.ChatMessageMapper;
import com.szd.boxgo.mapper.UserMapper;
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

    private final ChatMapper chatMapper;
    private final UserMapper userMapper;
    private final ChatMessageMapper messageMapper;

    @PersistenceContext
    private final EntityManager entityManager;

    @Transactional
    public ChatMessage createMessage(IncomingMessageDto newMessageDto, Long senderId) {
        Chat chat = chatRepoService.getChatIdOrCreate(
                newMessageDto.getInterlocutorId(),
                newMessageDto.getSegmentId());

        ChatMessage chatMessage = messageService.saveMessage(newMessageDto, chat, senderId);

        setChatLastMessage(chatMessage);

        return chatMessage;
    }

    public void setChatLastMessage(ChatMessage lastMessage) {
        chatRepoService.setChatLastMessage(lastMessage);
    }

    @Transactional
    public Page<ChatMessageDto> openChat(Long userId, Long segmentId, Long interlocutorId, Pageable pageable) {
        Chat chat = chatRepoService.getChatIdOrCreate(interlocutorId, segmentId);

        messageRepoService.setMessagesStatusToRead(chat.getId(), userId);
        eventPublisher.publishEvent(new ChatReadEvent(this, userId, chat.getId()));
        return messageRepoService.getMessagesByChatId(chat.getId(), pageable);
    }

    public ChatDto getChatDto(ChatMessage message, User interlocutor) {

        ChatInMessageDto dto = chatMapper.toChatInMessageDto(message.getChat());

        dto.setUnreadCount(
                message.getSender().getId().equals(interlocutor.getId())
                        ? message.getChat().getUnreadMessagesCount() : 0L
        );

        return ChatDto.builder()
                .interlocutor(userMapper.toDto(interlocutor))
                .chat(dto)
                .message(messageMapper.toDto(message))
                .build();
    }

    public Chat getChatById(Long id) {
        return chatRepoService.getChatById(id);
    }

    @Transactional
    public Page<ChatDto> getAll(Long userId, Pageable pageable) {
        Page<Chat> chats = chatRepoService.getUserChats(userId, pageable);

        return chats.map(chat -> {
            User counterpart = userId.equals(chat.getListingOwner().getId())
                    ? chat.getUser() : chat.getListingOwner();

            return getChatDto(chat.getLastMessage(), counterpart);
        });
    }
}
