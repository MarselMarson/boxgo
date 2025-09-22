package com.szd.boxgo.websocket.global;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.szd.boxgo.websocket.exception.MessageSerializationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextMessageMapper {
    private final ObjectMapper objectMapper;

    public TextMessage toTextMessage(Object message) {
        try {
            return new TextMessage(objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            String errorDetails = String.format(
                    "Failed to serialize message. Type: %s, Content: %s",
                    message.getClass().getName(),
                    message
            );
            log.error("Serialization error: {}. Details: {}", e.getMessage(), errorDetails);
            throw new MessageSerializationException("Invalid message format", e);
        }
    }
}
