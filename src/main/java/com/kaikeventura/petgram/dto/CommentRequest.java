package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @Schema(description = "The text content of the comment.", example = "So cute!", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank(message = "Comment text cannot be blank.")
        @Size(max = 500, message = "Comment cannot exceed 500 characters.")
        String text
) {
}
