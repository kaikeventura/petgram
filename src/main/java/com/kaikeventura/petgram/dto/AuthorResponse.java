package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Basic information about an author (pet).")
public record AuthorResponse(
        @Schema(description = "The unique identifier of the author.")
        UUID id,

        @Schema(description = "The name of the author.")
        String name,

        @Schema(description = "The avatar URL of the author.")
        String avatarUrl
) {
}
