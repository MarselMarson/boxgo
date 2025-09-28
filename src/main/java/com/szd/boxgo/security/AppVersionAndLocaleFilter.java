package com.szd.boxgo.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.exception.handler.ErrorResponse;
import com.szd.boxgo.i18n.AppLocale;
import com.szd.boxgo.i18n.AppMessages;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AppVersionAndLocaleFilter extends OncePerRequestFilter {

    public static final String HEADER_APP_VERSION = "X-App-Version";

    @Value("${app.min-supported-version:1.0.0}")
    private String minSupportedVersion;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain chain) throws ServletException, IOException {
        // resolve language once per request
        String lang = AppLocale.resolveLang(request);
        request.setAttribute(AppLocale.ATTR_LANG, lang);

        // version check
        String clientVersion = request.getHeader(HEADER_APP_VERSION);
        String websocket = request.getHeader("Upgrade");

        if (websocket == null && !checkVersion(clientVersion)) {
            // 426 with minimal JSON
            writeErrorResponse(response, request.getRequestURL().toString(), lang);
            return;
        }

        chain.doFilter(request, response);
    }

    private void writeErrorResponse(HttpServletResponse resp, String url, String lang) throws IOException {
        resp.setStatus(HttpStatus.UPGRADE_REQUIRED.value()); // 426
        resp.setContentType(MediaType.APPLICATION_JSON_VALUE);

        ErrorResponse errorResponse = new ErrorResponse(
                url,
                HttpStatus.UPGRADE_REQUIRED,
                "AppVersionTooLow",
                AppMessages.get(lang, AppMessages.KEY_APP_VERSION_EXPIRED)
        );

        objectMapper.writeValue(resp.getOutputStream(), errorResponse);
    }

    public boolean checkVersion(String clientVersion) {
        return !StringUtils.isBlank(clientVersion) && !isLower(clientVersion.trim(), minSupportedVersion);
    }

    private boolean isLower(String v, String min) {
        int[] a = parse(v);
        int[] b = parse(min);
        for (int i = 0; i < 3; i++) {
            if (a[i] < b[i]) return true;
            if (a[i] > b[i]) return false;
        }
        return false;
    }

    private int[] parse(String v) {
        String[] parts = v.split("\\.");
        int[] n = new int[]{0,0,0};
        for (int i = 0; i < Math.min(parts.length, 3); i++) {
            String p = parts[i].replaceAll("[^0-9]", "");
            try { n[i] = Integer.parseInt(p); } catch (Exception ignored) {}
        }
        return n;
    }
}
