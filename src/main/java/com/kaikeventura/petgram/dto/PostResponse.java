package com.kaikeventura.petgram.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PostResponse(
        UUID id,
        String photoUrl,
        String caption,
        AuthorResponse author,
        long likeCount,
        List<CommentResponse> comments,
        LocalDateTime createdAt
) {
}
