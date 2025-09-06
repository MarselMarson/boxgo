package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

    @Column(name = "is_archived")
    @Builder.Default
    Boolean isArchived = false;

    @Column(name = "has_available_packages")
    @Builder.Default
    Boolean hasAvailablePackages = false;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "next_departure_at")
    OffsetDateTime nextDepartureAt;

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

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Package> packages = new ArrayList<>();

    @OneToMany(mappedBy = "listing", cascade = CascadeType.ALL, orphanRemoval = true)
    List<RouteSegment> routeSegments = new ArrayList<>();

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
