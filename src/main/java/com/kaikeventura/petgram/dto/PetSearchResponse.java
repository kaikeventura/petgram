package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A summarized pet profile for search results.")
public record PetSearchResponse(
        @Schema(description = "The unique identifier of the pet.")
        UUID id,

        @Schema(description = "The name of the pet.")
        String name,

        @Schema(description = "The breed of the pet.")
        String breed
) {
}
