package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PetRequest(
        @Schema(description = "Name of the pet.", example = "Rex", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Name cannot be blank")
        String name,

        @Schema(description = "Breed of the pet.", example = "Golden Retriever", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Breed cannot be blank")
        String breed,

        @Schema(description = "Pet's birth date. Cannot be in the future.", example = "2022-01-15", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotNull(message = "Birth date cannot be null")
        @PastOrPresent(message = "Birth date cannot be in the future")
        LocalDate birthDate
) {
}
