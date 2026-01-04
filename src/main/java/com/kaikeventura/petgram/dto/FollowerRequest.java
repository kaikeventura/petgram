package com.kaikeventura.petgram.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record FollowerRequest(
    @NotNull
    UUID requesterPetId
) {}
