package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @Schema(description = "User's registered email address.", example = "john.doe@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank @Email
        String email,

        @Schema(description = "User's password.", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String password
) {
}
