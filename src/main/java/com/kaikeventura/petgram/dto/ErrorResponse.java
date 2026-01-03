package com.kaikeventura.petgram.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@Schema(description = "Standard API error response structure.")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ErrorResponse(
        @Schema(description = "Timestamp of when the error occurred.", example = "2024-01-01T10:00:00")
        LocalDateTime timestamp,

        @Schema(description = "The HTTP status code.", example = "404")
        int status,

        @Schema(description = "A short, human-readable error title.", example = "Not Found")
        String error,

        @Schema(description = "A detailed error message explaining the cause.", example = "User not found.")
        String message,

        @Schema(description = "Validation errors, if any.", nullable = true)
        Map<String, String> validationErrors
) {
    // Constructor for general errors
    public ErrorResponse(int status, String error, String message) {
        this(LocalDateTime.now(), status, error, message, null);
    }

    // Constructor for validation errors
    public ErrorResponse(int status, String error, String message, Map<String, String> validationErrors) {
        this(LocalDateTime.now(), status, error, message, validationErrors);
    }
}
