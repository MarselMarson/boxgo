package com.szd.boxgo.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Профиль пользователя")
public class UserProfileDto implements Serializable {
    @Schema(description = "пользователь")
    UserDto user;
    @Schema(description = "выбит из аккаунта")
    Boolean logout;
}
