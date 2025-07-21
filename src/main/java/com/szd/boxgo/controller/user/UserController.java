package com.szd.boxgo.controller.user;

import com.szd.boxgo.dto.auth.AuthUserId;
import com.szd.boxgo.dto.user.*;
import com.szd.boxgo.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Операции с пользователем")
public class UserController {
    private final UserManagementService userService;

    @Operation(summary = "Профиль")
    @GetMapping("/me")
    public UserDto profile(@AuthUserId Long authUserId) {
        return userService.getProfile(authUserId);
    }

    @PatchMapping("/me")
    @Operation(summary = "Обновить данные пользователя")
    public UserDto updateUser(@Valid @RequestBody UserPatchDto userDto,
                              @AuthUserId Long authUserId) {
        return userService.update(authUserId, userDto);
    }

    @Operation(summary = "Запрос на смену пароля")
    @PostMapping("/me/send-password-change-code")
    public void sendPasswordChangeCode(@AuthUserId Long userId) {
        userService.sendPasswordChangeCode(userId);
    }

    @Operation(summary = "Смена пароля")
    @PostMapping("/me/change-password")
    public void changePassword(@AuthUserId Long userId, @RequestBody ChangePasswordDto dto) {
        userService.changePassword(userId, dto);
    }

    @Operation(summary = "Смена пароля")
    @DeleteMapping("/me")
    public void deleteAccount(@AuthUserId Long userId, @RequestBody PasswordDto dto) {
        userService.deactivate(userId, dto.getPassword());
    }


}
