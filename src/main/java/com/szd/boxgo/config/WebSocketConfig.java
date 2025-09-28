package com.szd.boxgo.config;

import com.szd.boxgo.websocket.global.AuthHandshakeInterceptor;
import com.szd.boxgo.websocket.global.GlobalWebSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {
    private final GlobalWebSocketHandler globalWebSocketHandler;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(globalWebSocketHandler, "/websocket/global")
                .addInterceptors(new AuthHandshakeInterceptor())
                .setAllowedOrigins("*");
    }
}
