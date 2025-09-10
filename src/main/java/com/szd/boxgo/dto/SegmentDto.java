package com.szd.boxgo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.szd.boxgo.dto.user.UserDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Сегмент")
public class SegmentDto {
    Long id;
    Long listingId;
    Integer segmentsCount;

    CityDto fromCity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime departureLocalAt;
    CityDto toCity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime arrivalLocalAt;

    List<PackageDto> availablePackages;

    UserDto user;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime createdAt;
}
