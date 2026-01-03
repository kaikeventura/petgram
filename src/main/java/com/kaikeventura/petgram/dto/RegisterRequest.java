package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @Schema(description = "User's full name.", example = "John Doe", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 3, max = 50)
        String name,

        @Schema(description = "User's unique email address.", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email @Size(max = 100)
        String email,

        @Schema(description = "User's password. Must be between 6 and 100 characters.", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Size(min = 6, max = 100)
        String password
) {
}
