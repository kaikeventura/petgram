package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Information about a pending friend request.")
public record FriendshipRequestResponse(
        @Schema(description = "The ID of the pet that sent the request.")
        UUID requesterId,

        @Schema(description = "The name of the pet that sent the request.")
        String requesterName,

        @Schema(description = "The timestamp when the request was sent.")
        LocalDateTime requestedAt
) {
}
