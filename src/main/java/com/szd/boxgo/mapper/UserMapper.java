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

    UserDto toDto(User user);

    @Mapping(target = "createdAt", ignore = true)
    User toEntity(UserDto userDto);

    @Named("offsetToString")
    default String offsetToString(OffsetDateTime date) {
        return DateUtil.offsetToString(date);
    }

    void updateEntity(UserPatchDto dto, @MappingTarget User entity);

    default List<UserDto> toUserDots(List<User> users) {
        return users.stream()
                .map(this::toDto)
                .toList();
    }
}
