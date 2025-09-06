package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
}
