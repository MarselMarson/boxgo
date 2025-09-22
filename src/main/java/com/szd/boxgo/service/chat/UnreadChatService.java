package com.szd.boxgo.service.chat;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnreadChatService {
    private final ChatRepoService chatRepoService;

}
