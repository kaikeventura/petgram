package com.kaikeventura.petgram.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record FriendshipActionRequest(
        @NotNull UUID requesterPetId,
        @NotNull UUID addresseePetId
) {
}
