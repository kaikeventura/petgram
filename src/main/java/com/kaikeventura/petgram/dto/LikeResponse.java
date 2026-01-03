package com.kaikeventura.petgram.dto;

import java.util.UUID;

public record LikeResponse(
        UUID userId,
        String userName
) {
}
