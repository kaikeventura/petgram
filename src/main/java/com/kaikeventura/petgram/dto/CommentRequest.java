package com.kaikeventura.petgram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank(message = "Comment text cannot be blank.")
        @Size(max = 500, message = "Comment cannot exceed 500 characters.")
        String text
) {
}
