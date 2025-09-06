package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.CityDto;
import com.szd.boxgo.entity.City;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CityMapper {
    CityMapper INSTANCE = Mappers.getMapper(CityMapper.class);

    @Mapping(target = "id", source = "id")
    CityDto toCityDto(City city);
}
