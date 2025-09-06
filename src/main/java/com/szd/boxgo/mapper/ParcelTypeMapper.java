package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.ParcelTypeDto;
import com.szd.boxgo.entity.ParcelType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ParcelTypeMapper {
    ParcelTypeMapper INSTANCE = Mappers.getMapper(ParcelTypeMapper.class);

    ParcelTypeDto toDto(ParcelType parcelType);
}
