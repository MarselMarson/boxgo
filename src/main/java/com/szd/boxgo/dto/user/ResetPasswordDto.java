package com.szd.boxgo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Восстановление пароля")
public class ResetPasswordDto {
    @Schema(description = "email", example = "a@a.a")
    String email;

    @Schema(description = "Код верификации", example = "123456")
    @NotBlank(message = "Код не может быть пустым")
    private String verificationCode;

    @Schema(description = "Новый пароль", example = "new_pass")
    @Size(min = 8, max = 24, message = "Длина пароля должна быть от 8 до 24 символов")
    @NotBlank
    private String newPassword;
}
