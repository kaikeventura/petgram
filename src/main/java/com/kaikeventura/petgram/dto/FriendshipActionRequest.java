package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

@Schema(description = "Request body for actions involving two pets, like sending a friend request or removing a friendship.")
public record FriendshipActionRequest(
        @Schema(description = "The ID of the pet initiating the action (the requester).", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        UUID requesterPetId,

        @Schema(description = "The ID of the pet receiving the action (the addressee).", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull
        UUID addresseePetId
) {
}
