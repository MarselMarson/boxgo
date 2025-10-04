package com.szd.boxgo.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@EqualsAndHashCode(of = {"city", "languageCode"})
public class CityTranslationId implements Serializable {
    private Integer city;
    private String languageCode;
}