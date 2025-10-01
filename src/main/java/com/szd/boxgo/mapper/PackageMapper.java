package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.NewPackageDto;
import com.szd.boxgo.dto.PackageDto;
import com.szd.boxgo.entity.Package;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.StreamSupport;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PackageMapper {
    PackageMapper INSTANCE = Mappers.getMapper(PackageMapper.class);

    PackageDto toDto(Package pckg);

    Package toEntity(NewPackageDto pckgDto);

    default List<PackageDto> toDtos(Iterable<Package> packages) {
        if (packages == null) {
            return List.of();
        }
        return StreamSupport.stream(packages.spliterator(), false)
                .map(this::toDto)
                .toList();
    }
}
