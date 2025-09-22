package com.szd.boxgo.websocket.global.auth;

import com.szd.boxgo.entity.User;
import com.szd.boxgo.security.JwtService;
import com.szd.boxgo.service.user.UserRepoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
class JwtWebSocketAuthenticator implements WebSocketAuthenticator {
    private final JwtService jwtService;
    private final UserRepoService userRepo;

    @Override
    @Transactional(readOnly = true)
    public User authenticate(String jwtToken) throws AuthenticationException, EntityNotFoundException {
        try {
            if (jwtToken == null || jwtToken.trim().isEmpty()) {
                throw new AuthenticationException("Token is missing");
            }

            if (!jwtService.isTokenNotExpired(jwtToken)) {
                throw new AuthenticationException("Token expired");
            }

            String username = jwtService.extractUserName(jwtToken);

            return userRepo.getUser(username)
                    .orElseThrow(() -> new EntityNotFoundException("User deleted or banned"));

        } catch (AuthenticationException | EntityNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new AuthenticationException("Invalid token");
        }
    }
}
