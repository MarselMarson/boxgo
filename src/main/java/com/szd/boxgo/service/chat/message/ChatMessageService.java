package com.szd.boxgo.service.chat.message;

import com.szd.boxgo.dto.chat.ChatInMessageDto;
import com.szd.boxgo.dto.chat.IncomingMessageDto;
import com.szd.boxgo.dto.chat.SendingMessageDto;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.chat.Chat;
import com.szd.boxgo.entity.chat.ChatMessage;
import com.szd.boxgo.entity.chat.MessageStatus;
import com.szd.boxgo.mapper.ChatMapper;
import com.szd.boxgo.mapper.ChatMessageMapper;
import com.szd.boxgo.mapper.UserMapper;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final UserMapper userMapper;
    private final ChatMapper chatMapper;
    private final ChatMessageMapper chatMessageMapper;
    private final ChatMessageRepoService messageRepoService;
    private final EntityManager entityManager;

    @Transactional
    public ChatMessage saveMessage(IncomingMessageDto newMessage, Chat chat, Long senderId) {
        User senderProxy = entityManager.getReference(User.class, senderId);
        Long recipientId = chat.getListingOwner().getId().equals(senderId)
                ? chat.getUser().getId()
                : chat.getListingOwner().getId();
        User recipientProxy = entityManager.getReference(User.class, recipientId);

        ChatMessage message = ChatMessage.builder()
                .chat(chat)
                .sender(senderProxy)
                .recipient(recipientProxy)
                .content(newMessage.getContent())
                .status(MessageStatus.SENT.toString())
                .frontendId(newMessage.getClientMessageId())
                .build();

        return messageRepoService.saveMessage(message);
    }

    @Transactional
    public SendingMessageDto getSendingMessageDto(ChatMessage message, User interlocutor,
                                                  Long unreadChatsCount, Long unreadChatsVersion) {
        ChatInMessageDto dto = chatMapper.toChatInMessageDto(message.getChat());

        dto.setUnreadCount(
                message.getSender().getId().equals(interlocutor.getId())
                        ? message.getChat().getUnreadMessagesCount() : 0L
        );

        return SendingMessageDto.builder()
                .type("chat.message")
                .interlocutor(userMapper.toDto(interlocutor))
                .chat(dto)
                .message(chatMessageMapper.toDto(message))
                .unreadChatsTotal(unreadChatsCount)
                .unreadChatsVersion(unreadChatsVersion)
                .build();
    }

    @Transactional
    public ChatMessage setMessageStatusToRead(Long messageId, Long senderId, Long chatId) {
        ChatMessage targetMessage = messageRepoService.getMessageById(messageId);
        if (!senderId.equals(targetMessage.getRecipient().getId())
                || !targetMessage.getChat().getId().equals(chatId)
        ) {
            throw new AccessDeniedException(String.format("User %d can't read message from chat %d sent by %d",
                    senderId, chatId, targetMessage.getSender().getId()));
        }

        targetMessage.setStatus(MessageStatus.READ.toString());

        messageRepoService.setMessagesStatusToRead(chatId, senderId, messageId);

        return targetMessage;
    }
}
