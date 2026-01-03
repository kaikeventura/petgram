package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Information about a user who liked a post.")
public record LikeResponse(
        @Schema(description = "The unique identifier of the user.")
        UUID userId,

        @Schema(description = "The name of the user.")
        String userName
) {
}
