package com.szd.boxgo.websocket.messages;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GlobalAnswer {
    String type;
    Integer count;
}
