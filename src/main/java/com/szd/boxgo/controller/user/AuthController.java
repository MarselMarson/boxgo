package com.szd.boxgo.controller.user;

import com.szd.boxgo.dto.auth.JwtAuthenticationResponseDto;
import com.szd.boxgo.dto.auth.SignInRequestDto;
import com.szd.boxgo.dto.auth.SignUpRequestDto;
import com.szd.boxgo.dto.user.EmailDto;
import com.szd.boxgo.dto.user.ResetPasswordDto;
import com.szd.boxgo.service.security.AuthenticationService;
import com.szd.boxgo.service.user.email.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Аутентификация")
@SecurityRequirements
public class AuthController {
    private final AuthenticationService authenticationService;
    private final EmailService emailService;

    @Operation(summary = "Регистрация пользователя")
    @PostMapping("/sign-up")
    public JwtAuthenticationResponseDto signUp(@RequestBody @Valid SignUpRequestDto request) {
        return authenticationService.signUp(request);
    }

    @Operation(summary = "Авторизация пользователя")
    @PostMapping("/sign-in")
    public JwtAuthenticationResponseDto signIn(@RequestBody @Valid SignInRequestDto request) {
        return authenticationService.signIn(request);
    }

    @Operation(summary = "Проверка почты на доступность")
    @PostMapping("/check-email")
    public void isEmailAlreadyExists(@RequestBody @Valid EmailDto email) {
        emailService.checkEmail(email.getEmail());
    }

    @Operation(summary = "Восстановить пароль")
    @PostMapping("/reset-password")
    public void resetPassword(@RequestBody ResetPasswordDto resetPasswordDto) {
        authenticationService.resetPassword(resetPasswordDto);
    }

    @Operation(summary = "Проверить код восстановления пароля")
    @PostMapping("/check-reset-password-code")
    public void checkResetPasswordCode(@RequestBody ResetPasswordDto resetPasswordDto) {
        authenticationService.checkResetPasswordCode(resetPasswordDto);
    }

    @Operation(summary = "Запрос на восстановление пароля")
    @PostMapping("/request-password-reset")
    public void resetPasswordRequest(@RequestBody EmailDto email) {
        authenticationService.sendResetPasswordCode(email.getEmail());
    }

    @Operation(summary = "Отправить код верификации на email")
    @PostMapping("/send-registration-code")
    public void sendRegistrationCode(@RequestBody EmailDto email) {
        authenticationService.sendRegistrationCode(email.getEmail());
    }
}