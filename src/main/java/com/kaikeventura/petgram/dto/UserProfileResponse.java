package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Detailed information about a user's profile.")
public record UserProfileResponse(
        @Schema(description = "The unique identifier of the user.")
        UUID id,

        @Schema(description = "The user's full name.")
        String name,

        @Schema(description = "The user's unique email address.")
        String email
) {
}
