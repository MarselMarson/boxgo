package com.szd.boxgo.service;

import com.szd.boxgo.dto.*;
import com.szd.boxgo.entity.Package;
import com.szd.boxgo.entity.*;
import com.szd.boxgo.mapper.ListingMapper;
import com.szd.boxgo.mapper.PackageMapper;
import com.szd.boxgo.mapper.RouteSegmentMapper;
import com.szd.boxgo.repo.*;
import com.szd.boxgo.service.user.UserRepoService;
import com.szd.boxgo.util.DateUtil;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListingService {
    private final UserRepoService userService;

    private final CityRepo cityRepo;
    private final ParcelTypeRepo parcelTypeRepo;
    private final CategoryRepo categoryRepo;
    private final ListingRepo listingRepo;
    private final RouteSegmentRepo segmentRepo;
    private final PackageRepo packageRepo;

    private final ListingMapper listingMapper;
    private final RouteSegmentMapper segmentMapper;
    private final PackageMapper packageMapper;

    private final ListingSegmentSpecifications listingSegmentSpecifications;

    @Transactional
    public CreatedListingDto create(Long userId, NewListingDto newListing) {
        User user = userService.getUserById(userId);

        Set<RouteSegment> segments = new HashSet<>();
        Set<Package> packages = new HashSet<>();

        Listing listing = Listing.builder()
                .owner(user)
                .routeSegments(segments)
                .segmentsCount((short) (newListing.getPoints().size() - 1))
                .packages(packages)
                .build();

        fillPackages(packages, newListing.getPackages(), listing);
        fillSegments(segments, newListing.getPoints(), listing);
        fillSegmentPackages(newListing.getPackages(), segments);

        listingRepo.save(listing);

        return listingMapper.toCreatedDto(listing);
    }

    private void fillSegmentPackages(List<NewPackageDto> packages, Set<RouteSegment> segments) {
        short availableItemCount = 0;
        short availableEnvelopeCount = 0;
        short availableBoxCount = 0;
        short availableCarryOnCount = 0;
        short availableBaggageCount = 0;
        short availableOversizedCount = 0;

        for (NewPackageDto dto : packages) {
            switch (dto.getParcelTypeId()) {
                case 1: availableItemCount++; break;
                case 2: availableEnvelopeCount++; break;
                case 3: availableBoxCount++; break;
                case 4: availableCarryOnCount++; break;
                case 5: availableBaggageCount++; break;
                case 6: availableOversizedCount++; break;
            }
        }

        for (RouteSegment segment : segments) {
            segment
                    .setAvailableItemCount(availableItemCount)
                    .setAvailableEnvelopeCount(availableEnvelopeCount)
                    .setAvailableBoxCount(availableBoxCount)
                    .setAvailableCarryOnCount(availableCarryOnCount)
                    .setAvailableBaggageCount(availableBaggageCount)
                    .setAvailableOversizedCount(availableOversizedCount);
        }
    }

    public void validatePoints(List<PointDto> points, List<Long> cityIds, Map<Long, City> cities) {
        if (points == null || points.size() < 2) {
            throw new IllegalArgumentException("At least 2 points required");
        }

        if (points.getFirst().getDepartureAt() == null) {
            throw new IllegalArgumentException("First point must have departureAt (UTC)");
        }
        if (points.getLast().getArrivalAt() == null) {
            throw new IllegalArgumentException("Last point must have arrivalAt (UTC)");
        }

        for (int i = 1; i < points.size() - 1; i++) {
            PointDto p = points.get(i);
            if (p.getArrivalAt() == null || p.getDepartureAt() == null) {
                throw new IllegalArgumentException("Middle points must have both arrivalAt and departureAt (UTC)");
            }
        }

        if (new HashSet<>(cityIds).size() != cityIds.size()) {
            throw new IllegalArgumentException("Cities can't repeat");
        }

        if (cities.size() != cityIds.size()) {
            throw new EntityNotFoundException("Some cities not found");
        }

        for (int i = 0; i < points.size() - 2; i++) {
            PointDto a = points.get(i);
            PointDto b = points.get(i + 1);

            if (!a.getDepartureAt().isBefore(b.getArrivalAt())) {
                throw new IllegalArgumentException("prev point departureAt must be < next point arrivalAt");
            }
        }
    }

    private void fillSegments(Set<RouteSegment> segments, List<PointDto> points, Listing listing) {
        List<Long> cityIds = points.stream().map(PointDto::getCityId).toList();
        Map<Long, City> cities = cityRepo.findAllById(cityIds)
                .stream().collect(Collectors.toMap(City::getId, c -> c));

        validatePoints(points, cityIds, cities);

        for (short i = 0; i < points.size() - 1; i++) {
            for (short j = (short) (i + 1); j < points.size(); j++) {
                PointDto from = points.get(i);
                PointDto to = points.get(j);

                if (j > i + 1 && !from.getDepartureAt().isBefore(to.getArrivalAt())) {
                    throw new IllegalArgumentException("Non-adjacent: departureAt[i] must be < arrivalAt[j]");
                }

                City fromCity = cities.get(from.getCityId());
                City toCity = cities.get(to.getCityId());

                LocalDateTime departureLocalDate = DateUtil.toLocalDate(from.getDepartureAt(), fromCity.getTimezone());
                LocalDateTime arrivalLocalDate = DateUtil.toLocalDate(to.getArrivalAt(), toCity.getTimezone());
                OffsetDateTime visibleUntilUtc = DateUtil.local2359ToUtc(arrivalLocalDate.toLocalDate(), toCity.getTimezone());

                if (i == 0 && j == 1) {
                    listing.setFirstFromCity(fromCity);
                    listing.setFirstDepartureLocalAt(departureLocalDate);
                }
                if (i == 0 && j == points.size() - 1) {
                    listing.setLastToCity(toCity);
                    listing.setLastArrivalLocalAt(arrivalLocalDate);
                }

                Short order = (j - i == 1) ? j : null;

                RouteSegment segment = RouteSegment.builder()
                        .listing(listing)
                        .owner(listing.getOwner())
                        .order(order)
                        .totalSegmentsCount((short) (points.size() - 1))
                        .departureCity(fromCity)
                        .departureAt(from.getDepartureAt())
                        .departureLocalAt(departureLocalDate)
                        .arrivalCity(toCity)
                        .arrivalAt(to.getArrivalAt())
                        .arrivalLocalAt(arrivalLocalDate)
                        .archiveAt(visibleUntilUtc)
                        .build();

                segments.add(segment);
            }
        }
    }

    private void fillPackages(Set<Package> packages, List<NewPackageDto> newPackages, Listing listing) {
        int order = 0;
        for (NewPackageDto dto : newPackages) {
            order++;
            ParcelType parcelType = parcelTypeRepo.findById(dto.getParcelTypeId())
                            .orElseThrow(() -> new EntityNotFoundException("parcel type didn't found"));
            List<Category> categoriesFromDb = categoryRepo.findAllById(dto.getCategoryIds());

            if (categoriesFromDb.size() != dto.getCategoryIds().size()) {
                throw new EntityNotFoundException("Some categories not found");
            }

            Set<Category> categories = new HashSet<>(categoriesFromDb);

            //TODO validate size etc
            packages.add(Package.builder()
                    .order(order)
                    .listing(listing)
                    .parcelType(parcelType)
                    .categories(categories)
                    .length(dto.getLength())
                    .width(dto.getWidth())
                    .height(dto.getHeight())
                    .weight(dto.getWeight())
                    .quantity(dto.getQuantity())
                    .price(dto.getPrice())
                    .build());
        }
    }

    public ListingDto getById(Long id) {
        Listing listing = findById(id);
        return listingMapper.toDto(listing);
    }

    public Listing findById(Long id) {
        return listingRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("Listing with id " + id + " doesn't exists"));
    }

    public Page<SegmentDto> getAll(Pageable pageable, ListingSegmentFilter filter) {
        Specification<RouteSegment> spec = listingSegmentSpecifications.byFilter(filter);
        Page<RouteSegment> page = segmentRepo.findAll(spec, pageable);

        if (page.isEmpty()) {
            return page.map(segmentMapper::toDto);
        }

        Set<Listing> listings = page.getContent().stream()
                .map(RouteSegment::getListing)
                .collect(Collectors.toSet());

        Map<Long, List<PackageDto>> packagesByListingId = listings.stream()
                .flatMap(listing -> listing.getPackages().stream())
                .collect(Collectors.groupingBy(
                        p -> p.getListing().getId(),
                        Collectors.mapping(packageMapper::toDto, Collectors.toList())
                ));

        return page.map(segment -> {
            SegmentDto dto = segmentMapper.toDto(segment);
            dto.setAvailablePackages(packagesByListingId.getOrDefault(segment.getListing().getId(), Collections.emptyList()));
            return dto;
        });
    }

    public Page<CreatedListingDto> getCreated(Long userId, Pageable pageable) {
        Page<Listing> listings = listingRepo.findAllByOwnerIdOrderByFirstDepartureLocalAt(userId, pageable);
        return listings.map(listingMapper::toCreatedDto);
    }

    @Transactional
    public void archiveById(Long id, Long userId) {
        Listing listing = listingRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("id for current user not found or already archived"));
        if (!listing.getOwner().getId().equals(userId)) {
            throw new AccessDeniedException("no rights for delete");
        }
        listing.setIsArchived(true);
        listing.getRouteSegments().forEach(segment -> segment.setIsArchived(true));
    }

    public SegmentDto getSegmentById(Long id) {
        RouteSegment segment = segmentRepo.findWithPackagesById(id)
                .orElse(null);

        if (segment == null) {
            return new SegmentDto();
        }

        Set<Package> packages = segment.getListing().getPackages();

        SegmentDto dto = segmentMapper.toDto(segment);
        dto.setAvailablePackages(packageMapper.toDtos(packages));
        return dto;
    }
}
