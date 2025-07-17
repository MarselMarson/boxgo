package com.szd.boxgo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Пароль")
public class PasswordDto {
    @Schema(description = "Пароль")
    @NotBlank
    private String password;
}
