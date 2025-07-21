package com.szd.boxgo.service;

import com.szd.boxgo.dto.user.*;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.VerificationCode;
import com.szd.boxgo.entity.VerificationPurpose;
import com.szd.boxgo.exception.CodeNotFoundException;
import com.szd.boxgo.exception.DataValidationException;
import com.szd.boxgo.mapper.UserMapper;
import com.szd.boxgo.repo.UserRepo;
import com.szd.boxgo.service.security.PasswordService;
import com.szd.boxgo.service.user.UserRepoService;
import com.szd.boxgo.service.user.email.MailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.OffsetDateTime;

@Service
@Validated
@RequiredArgsConstructor
public class UserManagementService {
    private final UserRepo userRepo;
    private final UserRepoService repoService;
    private final UserMapper userMapper;
    private final PasswordService passwordService;
    private final VerificationCodeService verificationCodeService;
    private final MailService mailService;

    public User create(@Valid CreateUserDto newUserDto) {
        User user = User.builder()
                .email(newUserDto.getEmail())
                .password(newUserDto.getPassword())
                .firstName(newUserDto.getFirstName())
                .lastName(newUserDto.getLastName())
                .isDeleted(false)
                .build();

        userRepo.save(user);

        return user;
    }

    @Transactional
    public UserDto update(Long userId, @Valid UserPatchDto dto) {
        User user = repoService.getUserById(userId);

        userMapper.updateEntity(dto, user);

        User savedUser = userRepo.save(user);

        return userMapper.toDto(savedUser);
    }

    @Transactional
    public void deactivate(Long userId, String password) {
        User user = repoService.getUserById(userId);
        if (!passwordService.isPasswordsEqual(password, user.getPassword())) {
            throw new DataValidationException("Invalid password");
        }

        user.setEmail(null);
        user.setFirstName(null);
        user.setLastName(null);
        user.setPhotoUrl(null);
        user.setPassword(null);
        user.setIsDeleted(true);
        user.setDeletedAt(OffsetDateTime.now());

        userRepo.save(user);
    }

    public UserDto getProfile(Long authUserId) {
        User user = repoService.getUserById(authUserId);

        return userMapper.toDto(user);
    }

    public void changePassword(Long userId, ChangePasswordDto dto) {
        User user = repoService.getUserById(userId);

        boolean isVerificationCodeValid = verificationCodeService.checkVerificationCode(
                dto.getVerificationCode(),
                user.getEmail(),
                VerificationPurpose.PASSWORD_CHANGE.toString());

        if (isVerificationCodeValid) {
            if (dto.getOldPassword().equals(dto.getNewPassword())) {
                throw new DataValidationException("Passwords must be different");
            }
            if (!passwordService.isPasswordsEqual(dto.getOldPassword(), user.getPassword())) {
                throw new DataValidationException("Invalid old password");
            }
            String newEncodedPassword = passwordService.encodePassword(dto.getNewPassword());
            repoService.changePassword(userId, newEncodedPassword);
            verificationCodeService.useCode(dto.getVerificationCode(), user.getEmail());
        } else {
            throw new CodeNotFoundException("Неверный код подтверждения");
        }
    }

    @Transactional
    public void sendPasswordChangeCode(Long userId) {
        User user = repoService.getUserById(userId);

        VerificationCode code = verificationCodeService.createVerificationCode(
                user.getEmail(),
                VerificationPurpose.PASSWORD_CHANGE.toString());
        mailService.sendChangePassword(user.getEmail(), code.getCode());
    }
}
