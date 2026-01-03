package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "A summarized user profile for search results.")
public record UserSearchResponse(
        @Schema(description = "The unique identifier of the user.")
        UUID id,

        @Schema(description = "The name of the user.")
        String name
) {
}
