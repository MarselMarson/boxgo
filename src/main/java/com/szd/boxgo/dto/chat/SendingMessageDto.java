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
@Schema(description = "Отправляемое сообщение в чат")
public class SendingMessageDto {
    @JsonProperty("type")
    private String type;

    @JsonProperty("companion")
    private UserDto interlocutor;

    @JsonProperty("chat")
    private ChatInMessageDto chat;

    @JsonProperty("message")
    private ChatMessageDto message;

    @JsonProperty("unreadChatsTotal")
    private Long unreadChatsTotal;

    @JsonProperty("unreadChatsVersion")
    private Long unreadChatsVersion;
}
