package com.szd.boxgo.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "Новая заявка")
public class NewListingDto {
    List<PointDto> points;
    List<NewPackageDto> packages;
}
