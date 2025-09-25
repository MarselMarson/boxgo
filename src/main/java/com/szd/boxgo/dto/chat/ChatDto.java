package com.szd.boxgo.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.szd.boxgo.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "чат")
public class ChatDto {
    @JsonProperty("companion")
    private UserDto interlocutor;

    @JsonProperty("chat")
    private ChatInMessageDto chat;

    @JsonProperty("message")
    private ChatMessageDto message;
}
