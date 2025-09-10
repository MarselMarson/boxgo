package com.szd.boxgo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Сегмент")
public class CreatedSegmentDto {
    CityDto fromCity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime departureLocalAt;
    CityDto toCity;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    LocalDateTime arrivalLocalAt;
    Boolean isActive;
}
