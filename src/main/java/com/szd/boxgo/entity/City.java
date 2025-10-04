package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "city")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id"})
public class City {
    @Id
    Long id;

    @Column(name = "timezone")
    String timezone;

    @Column(name = "priority")
    Integer priority;

    @Column(name = "latlng")
    String latLng;

    @ManyToOne
    @JoinColumn(name = "countryid")
    @Fetch(FetchMode.JOIN)
    Country country;

    @OneToMany(mappedBy = "city", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<CityTranslation> translations = new HashSet<>();
}
