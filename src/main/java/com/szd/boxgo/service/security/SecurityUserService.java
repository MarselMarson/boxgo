package com.szd.boxgo.service.security;

import com.szd.boxgo.entity.User;
import com.szd.boxgo.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SecurityUserService {
    private final UserRepo repo;

    public User findUserByEmail(String email) {
        return repo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
    }
}
