package com.kaikeventura.petgram.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.UUID;

@Schema(description = "Detailed information about a pet's profile.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PetResponse(
        @Schema(description = "The unique identifier of the pet.")
        UUID id,

        @Schema(description = "The name of the pet.")
        String name,

        @Schema(description = "The breed of the pet.")
        String breed,

        @Schema(description = "The pet's birth date.")
        LocalDate birthDate,

        @Schema(description = "The public URL for the pet's avatar.")
        String avatarUrl,

        @Schema(description = "The unique identifier of the pet's owner.")
        UUID ownerId,

        @Schema(description = "The number of pets following this pet.")
        Long followerCount,

        @Schema(description = "The number of pets this pet is following.")
        Long followingCount
) {
}
