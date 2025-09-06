package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.NewPackageDto;
import com.szd.boxgo.dto.PackageDto;
import com.szd.boxgo.entity.Package;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PackageMapper {
    PackageMapper INSTANCE = Mappers.getMapper(PackageMapper.class);

    PackageDto toDto(Package pckg);

    Package toEntity(NewPackageDto pckgDto);

    default List<PackageDto> toDtos(List<Package> packages) {
        return packages.stream()
                .map(this::toDto)
                .toList();
    }
}
