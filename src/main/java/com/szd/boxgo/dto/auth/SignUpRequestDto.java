package com.szd.boxgo.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Schema(description = "Запрос на регистрацию")
@AllArgsConstructor
public class SignUpRequestDto {
    @Schema(description = "Адрес электронной почты", example = "johndoe@gmail.com")
    @Size(min = 5, max = 128, message = "Адрес электронной почты должен содержать от 5 до 128 символов")
    @NotBlank(message = "Адрес электронной почты не может быть пустыми")
    @Email(message = "Email адрес должен быть в формате user@example.com",
            regexp = "^[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")
    private String email;

    @Schema(description = "Имя пользователя", example = "John")
    @NotBlank(message = "Имя не может быть пустыми")
    private String firstName;

    @Schema(description = "Фамилия пользователя", example = "Doe")
    @NotBlank(message = "Фамилия не может быть пустыми")
    private String lastName;

    @Schema(description = "Пароль", example = "my_1secret1_Password")
    @Size(min = 8, max = 64, message = "Длина пароля должна быть от 8 до 64 символов")
    @NotBlank
    private String password;

    @Schema(description = "Код верификации", example = "123456")
    @NotBlank(message = "Код не может быть пустым")
    private String verificationCode;
}
