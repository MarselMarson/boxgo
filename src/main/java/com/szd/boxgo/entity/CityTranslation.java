package com.szd.boxgo.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "city_translation")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"city", "languageCode"})
@IdClass(CityTranslationId.class)
public class CityTranslation {
    @Id
    @ManyToOne
    @JoinColumn(name = "id", referencedColumnName = "id")
    private City city;

    @Id
    @Column(name = "languagecode")
    String languageCode;

    @Column(name = "name")
    String name;
}
