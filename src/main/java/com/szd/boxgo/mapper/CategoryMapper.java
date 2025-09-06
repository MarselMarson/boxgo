package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.CategoryDto;
import com.szd.boxgo.entity.Category;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryDto toDto(Category category);

    default List<CategoryDto> toDtos(List<Category> categories) {
        return categories.stream()
                .map(this::toDto)
                .toList();
    }
}
