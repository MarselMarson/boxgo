package com.szd.boxgo.service.user;

import com.szd.boxgo.entity.User;
import com.szd.boxgo.repo.UserRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRepoService {
    private final UserRepo userRepo;

    public User getUserById(Long id) {
        return userRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User id: " + id + " not found"));
    }

    public User getUserByEmail(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("User with email: " + email + " not found"));
    }

    public User getUserByEmailForLogin(String email) {
        return userRepo.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
    }

    public Optional<User> getUser(String email) {
        return userRepo.findByEmail(email);
    }

    public User getUserByEmailOrNull(String email) {
        return userRepo.findByEmail(email)
                .orElse(null);
    }

    public void changePassword(Long id, String newPassword) {
        User user = getUserById(id);
        user.setPassword(newPassword);
        userRepo.save(user);
    }
}
