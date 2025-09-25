package com.szd.boxgo.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Новое сообщение из чата")
public class IncomingMessageDto {
    @Schema(description = "тип сообщения")
    String type;
    @Schema(description = "id отрезка")
    Long segmentId;
    @Schema(description = "id собеседника")
    @JsonProperty("clientId")
    Long interlocutorId;
    @Schema(description = "id объявления")
    Long listingId;
    @Schema(description = "сообщение")
    String content;
    @Schema(description = "id frontend")
    UUID clientMessageId;
}
