package com.kaikeventura.petgram.dto;

import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The current status of a friendship between two pets.")
public record FriendshipStatusResponse(
        @Schema(description = "The friendship status. Can be PENDING, ACCEPTED, BLOCKED, or null if no relationship exists.",
                nullable = true, example = "ACCEPTED")
        FriendshipStatus status
) {
}
