package com.szd.boxgo.dto.websocket.global;

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
@Schema(description = "уведомление подключения")
public class ConnectionDto {
    @Schema(description = "тип уведомления")
    String type;
    @Schema(description = "уведомление о подключении к вебсокету")
    int code;
    @Schema(description = "Сообщение")
    String message;
}
