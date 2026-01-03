package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Detailed information about a post.")
public record PostResponse(
        @Schema(description = "The unique identifier of the post.")
        UUID id,

        @Schema(description = "The public URL of the post's photo.")
        String photoUrl,

        @Schema(description = "The caption text for the post.")
        String caption,

        @Schema(description = "The author of the post.")
        AuthorResponse author,

        @Schema(description = "Total number of likes on the post.")
        long likeCount,

        @Schema(description = "Total number of comments on the post.")
        long commentCount,

        @Schema(description = "The timestamp when the post was created.")
        LocalDateTime createdAt
) {
}
