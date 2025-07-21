package com.szd.boxgo.service.security;

import com.szd.boxgo.dto.auth.JwtAuthenticationResponseDto;
import com.szd.boxgo.dto.auth.SignInRequestDto;
import com.szd.boxgo.dto.auth.SignUpRequestDto;
import com.szd.boxgo.dto.user.CreateUserDto;
import com.szd.boxgo.dto.user.ResetPasswordDto;
import com.szd.boxgo.dto.user.UserDto;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.VerificationCode;
import com.szd.boxgo.entity.VerificationPurpose;
import com.szd.boxgo.exception.CodeNotFoundException;
import com.szd.boxgo.mapper.UserMapper;
import com.szd.boxgo.security.JwtService;
import com.szd.boxgo.service.UserManagementService;
import com.szd.boxgo.service.VerificationCodeService;
import com.szd.boxgo.service.user.UserRepoService;
import com.szd.boxgo.service.user.email.EmailService;
import com.szd.boxgo.service.user.email.MailService;
import com.szd.boxgo.util.DateUtil;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserMapper userMapper;
    private final EmailService emailService;
    private final UserManagementService userManagementService;
    private final UserRepoService userRepoService;
    private final MailService mailService;
    private final JwtService jwtService;
    private final PasswordService passwordService;
    private final VerificationCodeService verificationCodeService;

    /**
     * Регистрация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    @Transactional
    public JwtAuthenticationResponseDto signUp(@Valid SignUpRequestDto request) {
        if (emailService.isEmailAlreadyExist(request.getEmail())) {
            throw new BadCredentialsException("Email " + request.getEmail() + " уже зарегестрирован");
        }

        boolean isVerificationCodeValid = verificationCodeService.checkVerificationCode(
                request.getVerificationCode(),
                request.getEmail(),
                VerificationPurpose.REGISTRATION.toString());

        if (isVerificationCodeValid) {
            CreateUserDto signUpUser = CreateUserDto.builder()
                    .email(request.getEmail())
                    .password(passwordService.encodePassword(request.getPassword()))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .build();

            User createdUser = userManagementService.create(signUpUser);
            verificationCodeService.useCode(request.getVerificationCode(), request.getEmail());

            UserDto userDto = userMapper.toDto(createdUser);

            String jwt = jwtService.generateToken(createdUser);
            Date jwtExpiration = jwtService.getExpiration(jwt);

            return new JwtAuthenticationResponseDto(
                    jwt,
                    DateUtil.dateToString(jwtExpiration),
                    jwt,
                    DateUtil.dateToString(jwtExpiration),
                    userDto
            );
        } else {
            throw new BadCredentialsException("Код верификации недействителен");
        }
    }

    /**
     * Аутентификация пользователя
     *
     * @param request данные пользователя
     * @return токен
     */
    public JwtAuthenticationResponseDto signIn(SignInRequestDto request) {
        var user = userRepoService
                .getUserByEmailForLogin(request.getEmail());
        if (!passwordService.isPasswordsEqual(request.getPassword(), user.getPassword())) {
            throw new BadCredentialsException("Invalid username or password");
        }

        UserDto userDto = userMapper.toDto(user);

        String jwt = jwtService.generateToken(user);
        Date jwtExpiration = jwtService.getExpiration(jwt);

        return new JwtAuthenticationResponseDto(
                jwt,
                DateUtil.dateToString(jwtExpiration),
                jwt,
                DateUtil.dateToString(jwtExpiration),
                userDto
        );
    }

    @Transactional
    public void resetPassword(ResetPasswordDto resetPasswordDto) {
        User user = userRepoService.getUserByEmail(resetPasswordDto.getEmail());

        boolean isVerificationCodeValid = verificationCodeService.checkVerificationCode(
                resetPasswordDto.getVerificationCode(),
                resetPasswordDto.getEmail(),
                VerificationPurpose.PASSWORD_RESET.toString());

        if (isVerificationCodeValid) {
            String newEncodedPassword = passwordService.encodePassword(resetPasswordDto.getNewPassword());
            userRepoService.changePassword(user.getId(), newEncodedPassword);
            verificationCodeService.useCode(resetPasswordDto.getVerificationCode(), resetPasswordDto.getEmail());
        } else {
            throw new CodeNotFoundException("Неверный код подтверждения");
        }
    }

    @Transactional
    public void sendResetPasswordCode(String email) {
        if (emailService.isEmailAlreadyExist(email)) {
            VerificationCode code = verificationCodeService.createVerificationCode(
                    email,
                    VerificationPurpose.PASSWORD_RESET.toString());
            mailService.sendResetPassword(email, code.getCode());
        }
    }

    @Transactional
    public void sendRegistrationCode(String email) {
        if (!emailService.isEmailAlreadyExist(email)) {
            VerificationCode code = verificationCodeService.createVerificationCode(
                    email,
                    VerificationPurpose.REGISTRATION.toString());
            mailService.sendConfirmationEmail(email, code.getCode());
        }
    }
}
