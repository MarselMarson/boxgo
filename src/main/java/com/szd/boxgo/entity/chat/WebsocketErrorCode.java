package com.szd.boxgo.entity.chat;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum WebsocketErrorCode {
    APP_VERSION_DEPRECATED(426),
    USER_DELETED_OR_BANNED(403),
    TOKEN_INVALID(401);

    private final int code;
}
