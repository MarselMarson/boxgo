package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.OffsetDateTime;
import java.util.Set;

@Entity
@Table(name = "packages")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class Package {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne
    @JoinColumn(name = "listing_id")
    Listing listing;

    @ManyToOne
    @JoinColumn(name = "parcel_type_id")
    @Fetch(FetchMode.JOIN)
    ParcelType parcelType;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(
            name = "package_categories",
            joinColumns = @JoinColumn(name = "package_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    @Fetch(FetchMode.JOIN)
    Set<Category> categories;

    @Column(name = "length")
    Integer length;

    @Column(name = "width")
    Integer width;

    @Column(name = "height")
    Integer height;

    @Column(name = "weight")
    Integer weight;

    @Column(name = "quantity")
    Integer quantity;

    @Column(name = "price")
    Integer price;

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
    @Column(name = "deleted_at")
    OffsetDateTime deletedAt;
}
