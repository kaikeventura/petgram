package com.kaikeventura.petgram.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record CommentResponse(
        UUID id,
        String text,
        AuthorResponse author,
        LocalDateTime createdAt
) {
}
