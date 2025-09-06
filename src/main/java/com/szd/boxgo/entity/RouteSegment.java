package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;

@Entity
@Table(name = "route_segments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class RouteSegment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    Listing listing;

    @ManyToOne
    @JoinColumn(name = "departure_city_id")
    @Fetch(FetchMode.JOIN)
    City departureCity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "departure_at")
    OffsetDateTime departureAt;

    @ManyToOne
    @JoinColumn(name = "arrival_city_id")
    @Fetch(FetchMode.JOIN)
    City arrivalCity;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "arrival_at")
    OffsetDateTime arrivalAt;

    @Column(name = "segment_order")
    Short order;

    @Column(name = "is_archived")
    @Builder.Default
    Boolean isArchived = false;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "archived_at")
    OffsetDateTime archivedAt;
}
