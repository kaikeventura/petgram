package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Information about a pet who liked a post.")
public record LikeResponse(
        @Schema(description = "The unique identifier of the pet.")
        UUID petId,

        @Schema(description = "The name of the pet.")
        String petName
) {
}
