package com.szd.boxgo.service.user.email;

import com.szd.boxgo.exception.EmailAlreadyExistsException;
import com.szd.boxgo.repo.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final UserRepo userRepo;

    public boolean isEmailAlreadyExist(String email) {
        return userRepo.existsByEmail(email);
    }

    public void checkEmail(String email) {
        if (isEmailAlreadyExist(email)) {
            throw new EmailAlreadyExistsException("Этот адрес электронной почты уже зарегистрирован");
        }
    }
}
