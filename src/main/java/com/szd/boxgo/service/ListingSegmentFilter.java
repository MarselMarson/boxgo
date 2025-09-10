package com.szd.boxgo.service;

import lombok.Data;

import java.time.LocalDate;

@Data
public class ListingSegmentFilter {
    Long fromCityId;
    Long toCityId;

    LocalDate departureLocalAtAfter;
    LocalDate departureLocalAtBefore;

    LocalDate arrivalLocalAtAfter;
    LocalDate arrivalLocalAtBefore;

    Integer availableItemCount;
    Integer availableCarryOnCount;
    Integer availableBaggageCount;
}
