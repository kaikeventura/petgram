package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Detailed information about a comment.")
public record CommentResponse(
        @Schema(description = "The unique identifier of the comment.")
        UUID id,

        @Schema(description = "The text content of the comment.")
        String text,

        @Schema(description = "The author of the comment.")
        AuthorResponse author,

        @Schema(description = "The timestamp when the comment was created.")
        LocalDateTime createdAt
) {
}
