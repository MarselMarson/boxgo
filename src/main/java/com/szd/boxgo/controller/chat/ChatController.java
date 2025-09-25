package com.szd.boxgo.controller.chat;

import com.szd.boxgo.dto.auth.AuthUserId;
import com.szd.boxgo.dto.chat.ChatDto;
import com.szd.boxgo.dto.chat.ChatMessageDto;
import com.szd.boxgo.service.chat.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {
    private final ChatService chatService;

    @GetMapping("/{segmentId}/{interlocutorId}")
    public Page<ChatMessageDto> openChat(
            @AuthUserId Long authUserId,
            @PathVariable(name = "segmentId") Long segmentId,
            @PathVariable(name = "interlocutorId") Long interlocutorId,
            @PageableDefault(size = 15) Pageable pageable) {
        return chatService.openChat(authUserId, segmentId, interlocutorId, pageable);
    }

    @GetMapping("/all")
    public Page<ChatDto> getAllChats(
            @AuthUserId Long userId,
            @PageableDefault(size = 20) Pageable pageable) {
        return chatService.getAll(userId, pageable);
    }
}
