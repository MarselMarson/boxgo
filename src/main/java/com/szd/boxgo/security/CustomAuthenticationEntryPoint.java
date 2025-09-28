package com.szd.boxgo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.exception.handler.ErrorResponse;
import com.szd.boxgo.i18n.AppLocale;
import com.szd.boxgo.i18n.AppMessages;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper mapper = new ObjectMapper();

    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String lang = (String) request.getAttribute(AppLocale.ATTR_LANG);
        if (lang == null) lang = AppLocale.resolveLang(request);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        // формируем сообщение через AppMessages
        String msg = AppMessages.get(lang, AppMessages.KEY_TOKEN_EXPIRED);

        // создаём ErrorResponse
        ErrorResponse errorResponse = new ErrorResponse(
                request.getRequestURL().toString(),
                org.springframework.http.HttpStatus.UNAUTHORIZED,
                "AuthenticationError",
                msg
        );

        try (OutputStream os = response.getOutputStream()) {
            mapper.writeValue(os, errorResponse);
        }
    }
}
