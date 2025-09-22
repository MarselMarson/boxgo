package com.szd.boxgo.websocket.global.auth;

import com.szd.boxgo.entity.User;
import jakarta.persistence.EntityNotFoundException;

import javax.naming.AuthenticationException;

public interface WebSocketAuthenticator {
    User authenticate(String jwtToken) throws AuthenticationException, EntityNotFoundException;
}
