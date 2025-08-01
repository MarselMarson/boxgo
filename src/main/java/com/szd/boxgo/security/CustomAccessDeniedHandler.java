package com.szd.boxgo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.exception.handler.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        ErrorResponse errorResponse = new ErrorResponse(
                request.getRequestURL().toString(),
                HttpStatus.FORBIDDEN,
                "AccessDeniedError",
                accessDeniedException.getMessage()
        );

        try (OutputStream outputStream = response.getOutputStream()) {
            new ObjectMapper().writeValue(outputStream, errorResponse);
        }
    }
}
