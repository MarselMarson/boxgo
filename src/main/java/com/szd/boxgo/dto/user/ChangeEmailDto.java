package com.szd.boxgo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Запрос на смену email")
public class ChangeEmailDto {
    @Schema(description = "Старый email", example = "example@example.com")
    @NotBlank
    private String newEmail;

    @Schema(description = "Код верификации", example = "123456")
    @NotBlank(message = "Код не может быть пустым")
    private String verificationCodeOldEmail;

    @Schema(description = "Код верификации", example = "123456")
    @NotBlank(message = "Код не может быть пустым")
    private String verificationCodeNewEmail;
}
