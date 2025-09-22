package com.szd.boxgo.dto.chat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.szd.boxgo.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Чат")
public class ChatDto {
    Long id;
    Long listingId;
    Long segmentId;

    Long fromCity;
    Long toCity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime departureLocalAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    LocalDateTime arrivalLocalAt;

    UserDto counterpart;

    Long lastMessageId;
    Long lastMessageSenderId;
    String lastMessageContent;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime lastMessageCreatedAt;

    Long unreadCount;
    Long version;
}
