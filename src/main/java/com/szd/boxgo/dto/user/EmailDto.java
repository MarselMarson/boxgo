package com.szd.boxgo.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Email")
public class EmailDto {
    @Schema(description = "email", example = "a@a.a")
    String email;
}
