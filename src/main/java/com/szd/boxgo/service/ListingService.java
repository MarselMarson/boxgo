package com.szd.boxgo.service;

import com.szd.boxgo.dto.ListingDto;
import com.szd.boxgo.dto.NewListingDto;
import com.szd.boxgo.dto.NewPackageDto;
import com.szd.boxgo.dto.PointDto;
import com.szd.boxgo.entity.Package;
import com.szd.boxgo.entity.*;
import com.szd.boxgo.mapper.ListingMapper;
import com.szd.boxgo.repo.CategoryRepo;
import com.szd.boxgo.repo.CityRepo;
import com.szd.boxgo.repo.ListingRepo;
import com.szd.boxgo.repo.ParcelTypeRepo;
import com.szd.boxgo.service.user.UserRepoService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    private final ListingMapper listingMapper;

    @Transactional
    public ListingDto create(Long userId, NewListingDto newListing) {
        User user = userService.getUserById(userId);

        List<RouteSegment> segments = new ArrayList<>();
        List<Package> packages = new ArrayList<>();

        Listing listing = Listing.builder()
                .owner(user)
                .routeSegments(segments)
                .packages(packages)
                .build();

        fillSegments(segments, newListing.getPoints(), listing);
        fillPackages(packages, newListing.getPackages(), listing);

        listingRepo.save(listing);

        return listingMapper.toDto(listing);
    }

    private void fillSegments(List<RouteSegment> segments, List<PointDto> points, Listing listing) {
        List<Long> citiesId = points.stream()
                .map(PointDto::getCityId)
                .toList();

        if (citiesId.size() != points.size()) {
            throw new IllegalArgumentException("Cities can't repeat");
        }

        List<City> citiesFromDb = cityRepo.findAllById(citiesId);

        if (citiesId.size() != citiesFromDb.size()) {
            throw new EntityNotFoundException("Some cities not found");
        }

        Map<Long, City> cities = citiesFromDb.stream()
                .collect(Collectors.toMap(City::getId, city -> city));

        OffsetDateTime prevDepartureDate = null;
        for (short i = 0; i < points.size(); i++) {
            PointDto pointI = points.get(i);
            if (i != 0) {
                if (!prevDepartureDate.isBefore(pointI.getArrivalAt())) {
                    throw new IllegalArgumentException("invalid date (1)");
                }
                if ((i != points.size() - 1) && (pointI.getDepartureAt().isBefore(pointI.getArrivalAt()))) {
                    throw new IllegalArgumentException("invalid date (2)");
                }
            }
            prevDepartureDate = points.get(i).getDepartureAt();

            for (short j = (short) (i + 1); j < points.size(); j++) {
                PointDto pointFrom = points.get(i);
                PointDto pointTo = points.get(j);

                short order = j - i == 1 ? j : 0;

                RouteSegment segment = RouteSegment.builder()
                        .listing(listing)
                        .order(order)
                        .departureCity(cities.get(pointFrom.getCityId()))
                        .departureAt(pointFrom.getDepartureAt())
                        .arrivalCity(cities.get(pointTo.getCityId()))
                        .arrivalAt(pointTo.getArrivalAt())
                        .build();

                segments.add(segment);
            }
        }
    }

    private void fillPackages(List<Package> packages, List<NewPackageDto> newPackages, Listing listing) {
        for (NewPackageDto dto : newPackages) {
            ParcelType parcelType = parcelTypeRepo.findById(dto.getParcelTypeId())
                            .orElseThrow(() -> new EntityNotFoundException("parcel type didn't found"));
            List<Category> categoriesFromDb = categoryRepo.findAllById(dto.getCategoryIds());

            if (categoriesFromDb.size() != dto.getCategoryIds().size()) {
                throw new EntityNotFoundException("Some categories not found");
            }

            Set<Category> categories = new HashSet<>(categoriesFromDb);

            //TODO validate size etc
            packages.add(Package.builder()
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
        Listing listing = listingRepo.findById(id).orElseThrow(() -> new EntityNotFoundException("no"));
        return listingMapper.toDto(listing);
    }

    public Page<ListingDto> getAll(Pageable pageable) {
        return listingRepo.findAll(pageable).map(listingMapper::toDto);
    }
}
