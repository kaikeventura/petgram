package com.kaikeventura.petgram.dto;

import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String name,
        String email
) {
}
