package com.kaikeventura.petgram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

public record PetRequest(
        @NotBlank(message = "Name cannot be blank")
        String name,

        @NotBlank(message = "Breed cannot be blank")
        String breed,

        @NotNull(message = "Birth date cannot be null")
        @PastOrPresent(message = "Birth date cannot be in the future")
        LocalDate birthDate
) {
}
