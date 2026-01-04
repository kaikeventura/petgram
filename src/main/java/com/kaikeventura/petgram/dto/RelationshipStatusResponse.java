package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Describes the relationship status between the current pet and a target pet.")
public record RelationshipStatusResponse(
        @Schema(description = "The detailed status of the relationship.")
        RelationshipStatus status
) {
}
