package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "A container for search results, separating users and pets.")
public record SearchResponse(
        @Schema(description = "A list of users matching the search query.")
        List<UserSearchResponse> users,

        @Schema(description = "A list of pets matching the search query.")
        List<PetSearchResponse> pets
) {
}
