package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.Accessors;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;

@Entity
@Table(name = "route_segments")
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id", "departureCity", "arrivalCity"})
public class RouteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "listing_id")
    Listing listing;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Fetch(FetchMode.JOIN)
    User owner;

    @Column(name = "segment_order")
    Short order;

    @Column(name = "total_segments_count")
    Short totalSegmentsCount;


    @ManyToOne
    @JoinColumn(name = "departure_city_id")
    @Fetch(FetchMode.JOIN)
    City departureCity;

    @Column(name = "departure_at")
    OffsetDateTime departureAt;

    @Column(name = "departure_local_at")
    LocalDateTime departureLocalAt;


    @ManyToOne
    @JoinColumn(name = "arrival_city_id")
    @Fetch(FetchMode.JOIN)
    City arrivalCity;

    @Column(name = "arrival_at")
    OffsetDateTime arrivalAt;

    @Column(name = "arrival_local_at")
    LocalDateTime arrivalLocalAt;


    @Builder.Default
    @Column(name = "available_item_count")
    Short availableItemCount = 0;
    @Builder.Default
    @Column(name = "available_envelope_count")
    Short availableEnvelopeCount = 0;
    @Builder.Default
    @Column(name = "available_box_count")
    Short availableBoxCount = 0;
    @Builder.Default
    @Column(name = "available_carry_on_count")
    Short availableCarryOnCount = 0;
    @Builder.Default
    @Column(name = "available_baggage_count")
    Short availableBaggageCount = 0;
    @Builder.Default
    @Column(name = "available_oversized_count")
    Short availableOversizedCount = 0;
    @Column(name = "available_total_count",
            insertable = false,
            updatable = false)
    Short availableTotalCount;


    @Column(name = "is_archived")
    @Builder.Default
    Boolean isArchived = false;

    @Column(name = "archive_at")
    OffsetDateTime archiveAt;

    @CreationTimestamp
    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Column(name = "archived_at")
    OffsetDateTime archivedAt;

    public boolean getIsNotArchived() {
        return !this.isArchived;
    }
}
