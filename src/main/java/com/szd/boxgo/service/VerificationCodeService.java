package com.szd.boxgo.service;

import com.szd.boxgo.entity.VerificationCode;
import com.szd.boxgo.exception.CodeNotFoundException;
import com.szd.boxgo.repo.VerificationCodeRepo;
import com.szd.boxgo.util.PasswordGeneratorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class VerificationCodeService {
    private final VerificationCodeRepo verificationCodeRepo;
    private final Clock clock;

    public VerificationCode createVerificationCode(String email, String purpose) {
        OffsetDateTime expiresAt = OffsetDateTime.now(clock).plusMinutes(11);
        return verificationCodeRepo.save(VerificationCode.builder()
                .code(PasswordGeneratorUtil.generateRandom6DigitNumber())
                .email(email)
                .purpose(purpose)
                .isUsed(false)
                .expiresAt(expiresAt)
                .build());
    }

    public VerificationCode getVerificationCode(String code, String email) {
        return verificationCodeRepo.findByCodeAndEmail(code, email)
                .orElseThrow(() -> new CodeNotFoundException("Неверный код подтверждения"));
    }

    public void useCode(String verificationCode, String email) {
        VerificationCode code = getVerificationCode(verificationCode, email);
        code.setIsUsed(true);
        verificationCodeRepo.save(code);
    }
}
