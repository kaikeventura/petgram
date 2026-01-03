package com.kaikeventura.petgram.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String photoUrl,
        String caption,
        AuthorResponse author,
        long likeCount,
        long commentCount, // Changed from List<CommentResponse>
        LocalDateTime createdAt
) {
}
