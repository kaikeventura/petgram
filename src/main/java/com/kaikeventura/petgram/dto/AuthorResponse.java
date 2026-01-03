package com.kaikeventura.petgram.dto;

import java.util.UUID;

public record AuthorResponse(
        UUID id,
        String name
) {
}
