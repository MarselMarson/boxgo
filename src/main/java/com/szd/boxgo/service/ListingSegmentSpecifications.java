package com.szd.boxgo.service;

import com.szd.boxgo.entity.RouteSegment;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ListingSegmentSpecifications {

    public Specification<RouteSegment> byFilter(ListingSegmentFilter f) {
        return (root, query, cb) -> {
            List<Predicate> ps = new ArrayList<>();

            ps.add(cb.isFalse(root.get("isArchived")));
            ps.add(cb.greaterThan(root.get("availableTotalCount"), 0));

            if (f.getFromCityId() != null) {
                ps.add(cb.equal(root.get("departureCity").get("id"), f.getFromCityId()));
            }
            if (f.getToCityId() != null) {
                ps.add(cb.equal(root.get("arrivalCity").get("id"), f.getToCityId()));
            }

            if (f.getDepartureLocalAtAfter() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("departureLocalAt"), f.getDepartureLocalAtAfter()));
            }
            if (f.getDepartureLocalAtBefore() != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("departureLocalAt"), f.getDepartureLocalAtBefore()));
            }

            if (f.getArrivalLocalAtAfter() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("arrivalLocalAt"), f.getArrivalLocalAtAfter()));
            }
            if (f.getArrivalLocalAtBefore() != null) {
                ps.add(cb.lessThanOrEqualTo(root.get("arrivalLocalAt"), f.getArrivalLocalAtBefore()));
            }

            // Минимальные доступности
            if (f.getAvailableItemCount() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("availableItemCount"), f.getAvailableItemCount()));
            }
            if (f.getAvailableCarryOnCount() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("availableCarryOnCount"), f.getAvailableCarryOnCount()));
            }
            if (f.getAvailableBaggageCount() != null) {
                ps.add(cb.greaterThanOrEqualTo(root.get("availableBaggageCount"), f.getAvailableBaggageCount()));
            }

            return cb.and(ps.toArray(Predicate[]::new));
        };
    }
}
