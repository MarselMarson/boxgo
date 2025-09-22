package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.chat.ChatMessageDto;
import com.szd.boxgo.entity.chat.ChatMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMessageMapper {
    ChatMessageMapper INSTANCE = Mappers.getMapper(ChatMessageMapper.class);

    @Mapping(target = "clientMessageId", source = "frontendId")
    @Mapping(target = "senderId", source = "sender.id")
    @Mapping(target = "content", source = "content")
    @Mapping(target = "status", source = "status")
    @Mapping(target = "version", source = "version")
    @Mapping(target = "createdAt", source = "createdAt")
    ChatMessageDto toDto(ChatMessage message);
}
