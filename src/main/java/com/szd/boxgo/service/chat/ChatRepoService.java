package com.szd.boxgo.service.chat;

import com.szd.boxgo.entity.RouteSegment;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.entity.chat.Chat;
import com.szd.boxgo.repo.chat.ChatRepo;
import com.szd.boxgo.service.RouteSegmentService;
import com.szd.boxgo.service.user.UserRepoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRepoService {
    private final ChatRepo chatRepo;
    private final UserRepoService userRepoService;
    private final RouteSegmentService segmentService;

    public Chat getChatById(Long id) {
        return chatRepo.findById(id).orElseThrow(
                () -> new EntityNotFoundException("chat with id: " + id + " not found"));
    }

    @Transactional
    public Chat getChatIdOrCreate(Long userId, Long segmentId) {
        return getChat(userId, segmentId)
                .orElseGet(() -> createNewChat(userId, segmentId));
    }

    public Chat createNewChat(Long senderId, Long segmentId) {
        User sender = userRepoService.getUserById(senderId);
        RouteSegment segment = segmentService.findById(segmentId);
        User listingOwner = segment.getOwner();

        Chat chat = Chat.builder()
                .user(sender)
                .listingOwner(listingOwner)
                .listing(segment.getListing())
                .segment(segment)

                .fromCity(segment.getDepartureCity())
                .toCity(segment.getArrivalCity())
                .departureLocalAt(segment.getDepartureLocalAt())
                .arrivalLocalAt(segment.getArrivalLocalAt())

                .lastMessage(null)
                .lastMessageSender(null)
                .lastMessageContent(null)
                .lastMessageCreatedAt(null)

                .unreadMessagesCount(0L)
                .isActive(true)
                .build();

        return chatRepo.save(chat);
    }

    public Optional<Chat> getChat(Long userId, Long segmentId) {
        return chatRepo.findByUserIdAndSegmentId(userId, segmentId);
    }
}