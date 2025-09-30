package com.szd.boxgo.service.user;

import com.szd.boxgo.dto.user.*;
import com.szd.boxgo.entity.File;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.VerificationCode;
import com.szd.boxgo.entity.VerificationPurpose;
import com.szd.boxgo.exception.CodeNotFoundException;
import com.szd.boxgo.exception.DataValidationException;
import com.szd.boxgo.mapper.UserMapper;
import com.szd.boxgo.repo.UserRepo;
import com.szd.boxgo.service.FileService;
import com.szd.boxgo.service.VerificationCodeService;
import com.szd.boxgo.service.security.PasswordService;
import com.szd.boxgo.service.user.email.EmailService;
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
    private final FileService fileService;
    private final EmailService emailService;

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

        if (!user.getPhotoUrl().equals(dto.getPhotoUrl())) {
            if (dto.getPhotoUrl() != null && !dto.getPhotoUrl().isBlank()) {
                String fileName = fileService.getNameFromUrl(dto.getPhotoUrl());
                File photo = fileService.getByFileName(fileName);
                user.setPhoto(photo);
                user.setPhotoUrl(photo.getUrl());
            } else {
                user.setPhoto(null);
                user.setPhotoUrl(null);
            }
        }

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
        user.setPassword(null);

        user.setPhoto(null);
        user.setPhotoUrl(null);

        user.setIsDeleted(true);
        user.setDeletedAt(OffsetDateTime.now());

        userRepo.save(user);
    }

    public UserDto removePhotoUrl(Long userId) {
        User user = repoService.getUserById(userId);
        user.setPhoto(null);
        user.setPhotoUrl(null);

        User savedUser = userRepo.save(user);

        return userMapper.toDto(savedUser);
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

    @Transactional
    public void sendEmailChangeCode(Long userId) {
        User user = repoService.getUserById(userId);

        VerificationCode codeOldEmail = verificationCodeService.createVerificationCode(
                user.getEmail(),
                VerificationPurpose.EMAIL_CHANGE.toString());
        mailService.sendChangeEmail(user.getEmail(), codeOldEmail.getCode());
    }

    @Transactional
    public void changeEmail(Long userId, ChangeEmailDto dto) {
        User user = repoService.getUserById(userId);
        if (dto.getNewEmail().equals(user.getEmail())) {
            throw new DataValidationException("Emails must be different");
        }
        emailService.checkEmail(dto.getNewEmail());

        boolean isVerificationCodeValid = verificationCodeService.checkVerificationCode(
                dto.getVerificationCodeOldEmail(),
                user.getEmail(),
                VerificationPurpose.EMAIL_CHANGE.toString());

        if (isVerificationCodeValid) {
            verificationCodeService.useCode(dto.getVerificationCodeOldEmail(), user.getEmail());
        } else {
            throw new CodeNotFoundException("Неверный код подтверждения");
        }

        VerificationCode codeNewEmail = verificationCodeService.createVerificationCode(
                dto.getNewEmail(),
                VerificationPurpose.EMAIL_PROVE.toString());
        mailService.sendChangeEmail(dto.getNewEmail(), codeNewEmail.getCode());
    }

    @Transactional
    public void proveEmail(Long userId, ChangeEmailDto dto) {
        User user = repoService.getUserById(userId);

        boolean isVerificationCodeValid = verificationCodeService.checkVerificationCode(
                dto.getVerificationCodeNewEmail(),
                dto.getNewEmail(),
                VerificationPurpose.EMAIL_PROVE.toString());

        if (isVerificationCodeValid) {
            verificationCodeService.useCode(dto.getVerificationCodeOldEmail(), user.getEmail());
            user.setEmail(dto.getNewEmail());
        } else {
            throw new CodeNotFoundException("Неверный код подтверждения");
        }
    }
}
