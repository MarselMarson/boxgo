package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.CreatedListingDto;
import com.szd.boxgo.dto.ListingDto;
import com.szd.boxgo.dto.NewListingDto;
import com.szd.boxgo.entity.Listing;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        uses = {RouteSegmentMapper.class, PackageMapper.class})
public interface ListingMapper {


    @Mapping(target = "segments", source = "orderedRouteSegments")
    @Mapping(target = "availablePackages", source = "packages")
    @Mapping(target = "user", source = "owner")
    @Mapping(target = "isFavourite", constant = "false")
    ListingDto toDto(Listing listing);

    @Mapping(target = "segments", source = "orderedRouteSegments")
    @Mapping(target = "availablePackages", source = "packages")
    @Mapping(target = "fromCity", source = "firstFromCity")
    @Mapping(target = "departureLocalAt", source = "firstDepartureLocalAt")
    @Mapping(target = "toCity", source = "lastToCity")
    @Mapping(target = "arrivalLocalAt", source = "lastArrivalLocalAt")
    CreatedListingDto toCreatedDto(Listing listing);

    @Mapping(target = "createdAt", ignore = true)
    Listing toEntity(NewListingDto userDto);

    default List<ListingDto> toDtos(List<Listing> listings) {
        return listings.stream()
                .map(this::toDto)
                .toList();
    }
}
