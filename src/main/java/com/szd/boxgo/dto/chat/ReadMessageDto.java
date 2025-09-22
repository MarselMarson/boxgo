package com.szd.boxgo.dto.chat;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Прочтение сообщения")
public class ReadMessageDto {
    @Schema(description = "тип сообщения")
    String type;
    @Schema(description = "id объявления")
    Long listingId;
    @Schema(description = "id отрезка")
    Long segmentId;
    @Schema(description = "id собеседника")
    Long interlocutorId;
    @Schema(description = "id сообщения")
    Long messageId;
}
