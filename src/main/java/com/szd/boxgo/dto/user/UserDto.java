package com.szd.boxgo.dto.user;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Пользователь")
public class UserDto implements Serializable {
    @Schema(description = "id пользователя")
    Long id;
    @Schema(description = "email", example = "a@a.a")
    String email;
    @Schema(description = "Имя пользователя")
    String firstName;
    @Schema(description = "Фамилия пользователя")
    String lastName;
    @Schema(description = "Фотографии пользователя")
    String photoUrl;

    @Schema(description = "Дата создания аккаунта")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
    OffsetDateTime createdAt;
}
