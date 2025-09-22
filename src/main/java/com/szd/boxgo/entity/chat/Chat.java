package com.szd.boxgo.entity.chat;

import com.szd.boxgo.entity.City;
import com.szd.boxgo.entity.Listing;
import com.szd.boxgo.entity.RouteSegment;
import com.szd.boxgo.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "chats")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Chat {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_owner_id", nullable = false)
    private User listingOwner;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id", nullable = false)
    private Listing listing;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "segment_id", nullable = false)
    private RouteSegment segment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_city_id", nullable = false)
    private City fromCity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_city_id", nullable = false)
    private City toCity;

    @Column(name = "departure_local_at", nullable = false)
    private LocalDateTime departureLocalAt;

    @Column(name = "arrival_local_at", nullable = false)
    private LocalDateTime arrivalLocalAt;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_id")
    private ChatMessage lastMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "last_message_sender_id")
    private User lastMessageSender;

    @Column(name = "last_message_content", length = 4096)
    private String lastMessageContent;

    @Column(name = "last_message_created_at")
    private OffsetDateTime lastMessageCreatedAt;

    @Column(name = "unread_messages_count")
    @Builder.Default
    private Long unreadMessagesCount = 0L;

    @Column(name = "is_active")
    @Builder.Default
    private Boolean isActive = true;

    @Version
    private Long version;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private OffsetDateTime updatedAt;
}
