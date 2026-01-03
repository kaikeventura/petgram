package com.kaikeventura.petgram.dto;

import java.util.UUID;

public record UserSearchResponse(
        UUID id,
        String name
) {
}
