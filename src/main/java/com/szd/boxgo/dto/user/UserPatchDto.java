package com.szd.boxgo.dto.user;

import com.szd.boxgo.dto.validation.NullOrNotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Schema(description = "Запрос на изменение пользователя")
public class UserPatchDto {
    @Schema(description = "Имя пользователя")
    @NullOrNotBlank
    String firstName;
    @Schema(description = "Фамилия пользователя")
    @NullOrNotBlank
    String lastName;
    @Schema(description = "Название файла фото пользователя", example = "saved.png")
    String photoUrl;
}
