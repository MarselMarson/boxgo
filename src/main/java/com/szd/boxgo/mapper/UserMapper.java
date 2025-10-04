package com.szd.boxgo.mapper;

import com.szd.boxgo.dto.user.UserDto;
import com.szd.boxgo.dto.user.UserPatchDto;
import com.szd.boxgo.entity.User;
import com.szd.boxgo.util.DateUtil;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.time.OffsetDateTime;
import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

    @Mapping(target = "photoUrl", source = "photo.url")
    @Mapping(target = "lastSeen", source = "updatedAt", qualifiedByName = "offsetToMilliseconds")
    UserDto toDto(User user);

    @Named("offsetToMilliseconds")
    default Long offsetToMilliseconds(OffsetDateTime date) {
        if (date == null) {
            return null;
        }
        return date.toInstant().toEpochMilli();
    }

    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDto userDto);

    @Named("offsetToString")
    default String offsetToString(OffsetDateTime date) {
        return DateUtil.offsetToString(date);
    }

    @Mapping(target = "photo", ignore = true)
    @Mapping(target = "photoUrl", ignore = true)
    void updateEntity(UserPatchDto dto, @MappingTarget User entity);

    default List<UserDto> toUserDots(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }
}
