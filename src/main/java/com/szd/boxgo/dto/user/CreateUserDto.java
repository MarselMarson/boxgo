package com.szd.boxgo.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.validation.annotation.Validated;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Пользователь для создания")
@Validated
public class CreateUserDto {
    @Schema(description = "Адрес электронной почты", example = "johndoe@gmail.com")
    @Size(min = 5, max = 50, message = "Адрес электронной почты должен содержать от 5 до 50 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com",
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    String email;
    @Schema(description = "Пароль")
    @NotBlank
    String password;
    @Schema(description = "Имя")
    @NotBlank(message = "Имя не может быть пустыми")
    String firstName;
    @Schema(description = "Фамилия")
    @NotBlank(message = "Фамилия не может быть пустыми")
    String lastName;
}
