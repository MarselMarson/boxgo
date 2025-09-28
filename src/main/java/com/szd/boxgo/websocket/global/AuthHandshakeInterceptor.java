package com.szd.boxgo.websocket.global;

import com.szd.boxgo.i18n.AppLocale;
import com.szd.boxgo.security.AppVersionAndLocaleFilter;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

public class AuthHandshakeInterceptor implements HandshakeInterceptor {
    @Override
    public boolean beforeHandshake(@NotNull ServerHttpRequest request,
                                   @NotNull ServerHttpResponse response,
                                   @NotNull WebSocketHandler wsHandler,
                                   @NotNull Map<String, Object> attributes) throws Exception {
        HttpHeaders h = request.getHeaders();
        String appVersion = h.getFirst(AppVersionAndLocaleFilter.HEADER_APP_VERSION);
        String lang = h.getFirst(AppLocale.HEADER_LANG);
        if (lang == null) lang = h.getFirst(AppLocale.HEADER_LANG_ALT);
        if (lang == null) lang = h.getFirst(AppLocale.HEADER_ACCEPT_LANG);

        attributes.put(AppVersionAndLocaleFilter.HEADER_APP_VERSION, appVersion);
        attributes.put(AppLocale.ATTR_LANG, lang != null ? lang : "en");

        String authHeader = h.getFirst("Authorization");
        if (authHeader != null) {
            attributes.put("Authorization", authHeader);
        }

        return true;
    }

    @Override
    public void afterHandshake(@NotNull ServerHttpRequest request,
                               @NotNull ServerHttpResponse response,
                               @NotNull WebSocketHandler wsHandler,
                               @NotNull Exception exception) {
    }
}
