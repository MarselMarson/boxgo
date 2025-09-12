package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;

@Entity
@Table(name = "listings")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Listing {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    @Fetch(FetchMode.JOIN)
    User owner;

    @Column(name = "segments_count")
    Short segmentsCount;

    @Column(name = "is_archived")
    @Builder.Default
    Boolean isArchived = false;


    @ManyToOne
    @JoinColumn(name = "first_from_city_id")
    City firstFromCity;

    @Column(name = "first_departure_local")
    LocalDateTime firstDepartureLocalAt;

    @ManyToOne
    @JoinColumn(name = "last_to_city_id")
    City lastToCity;

    @Column(name = "last_arrival_local")
    LocalDateTime lastArrivalLocalAt;


    @CreationTimestamp
    @Column(name = "created_at")
    OffsetDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt;

    @Column(name = "archived_at")
    OffsetDateTime archivedAt;

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<Package> packages = new HashSet<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<RouteSegment> routeSegments = new HashSet<>();

    public List<RouteSegment> getOrderedRouteSegments() {
        if (routeSegments == null) {
            return Collections.emptyList();
        }

        return routeSegments.stream()
                .filter(segment -> segment.getOrder() != null && segment.getOrder() != 0)
                .sorted(Comparator.comparing(RouteSegment::getOrder))
                .toList();
    }
}
