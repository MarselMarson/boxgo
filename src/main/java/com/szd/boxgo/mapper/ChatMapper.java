package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.chat.ChatInMessageDto;
import com.szd.boxgo.entity.chat.Chat;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChatMapper {
    ChatMapper INSTANCE = Mappers.getMapper(ChatMapper.class);

    @Mapping(target = "listingId", source = "listing.id")
    @Mapping(target = "segmentId", source = "segment.id")
    @Mapping(target = "fromCity", source = "fromCity.id")
    @Mapping(target = "toCity", source = "toCity.id")
    @Mapping(target = "departureLocalAt", source = "departureLocalAt")
    @Mapping(target = "arrivalLocalAt", source = "arrivalLocalAt")
    @Mapping(target = "unreadCount", ignore = true)
    @Mapping(target = "version", source = "version")
    ChatInMessageDto toChatInMessageDto(Chat chat);
}
