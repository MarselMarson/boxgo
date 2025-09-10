package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.CreatedSegmentDto;
import com.szd.boxgo.dto.SegmentDto;
import com.szd.boxgo.entity.RouteSegment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = {CityMapper.class, UserMapper.class})
public interface RouteSegmentMapper {

    @Mapping(target = "listingId", source = "listing.id")
    @Mapping(target="segmentsCount", source="totalSegmentsCount")
    @Mapping(target = "fromCity", source = "departureCity")
    @Mapping(target = "toCity", source = "arrivalCity")
    @Mapping(target = "user", source = "owner")
    SegmentDto toDto(RouteSegment segment);

    @Mapping(target = "fromCity", source = "departureCity")
    @Mapping(target = "toCity", source = "arrivalCity")
    @Mapping(target = "isActive", source = "isNotArchived")
    CreatedSegmentDto toCreatedDto(RouteSegment segment);

    default List<SegmentDto> toDtos(List<RouteSegment> segments) {
        return segments.stream()
                .map(this::toDto)
                .toList();
    }
}
