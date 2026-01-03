package com.kaikeventura.petgram.dto;

import java.time.LocalDate;
import java.util.UUID;

public record PetResponse(
        UUID id,
        String name,
        String breed,
        LocalDate birthDate,
        UUID ownerId
) {
}
