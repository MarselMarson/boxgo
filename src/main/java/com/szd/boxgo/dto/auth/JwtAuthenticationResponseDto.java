package com.szd.boxgo.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.szd.boxgo.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Ответ c токеном доступа c id пользователя")
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JwtAuthenticationResponseDto {
    @Schema(description = "Токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String accessToken;
    @Schema(description = "Дата просрочки токена", example = "2025-07-13T21:45:00+00:00")
    private String accessTokenExpiresAt;
    @Schema(description = "Рефреш токен доступа", example = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJhZG1pbiIsImV4cCI6MTYyMjUwNj...")
    private String refreshToken;
    @Schema(description = "Дата просрочки рефреш токена", example = "2025-07-13T21:45:00+00:00")
    private String refreshTokenExpiresAt;

    @Schema(description = "Данные пользователя")
    private UserDto user;
}
