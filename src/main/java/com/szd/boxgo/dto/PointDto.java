package com.szd.boxgo.dto;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.OffsetDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Точка")
public class PointDto {
    Long cityId;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime departureAt;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    OffsetDateTime arrivalAt;
}
