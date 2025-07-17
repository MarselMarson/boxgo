package com.szd.boxgo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.exception.handler.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        ErrorResponse errorResponse = new ErrorResponse(
                request.getRequestURL().toString(),
                HttpStatus.UNAUTHORIZED,
                "AuthenticationError",
                authException.getMessage()
        );

        try (OutputStream outputStream = response.getOutputStream()) {
            new ObjectMapper().writeValue(outputStream, errorResponse);
        }
    }
}
