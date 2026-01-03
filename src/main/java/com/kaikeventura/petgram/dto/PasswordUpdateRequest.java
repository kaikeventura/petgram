package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
        @Schema(description = "The user's current (old) password.", example = "password123", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        String oldPassword,

        @Schema(description = "The new password. Must be between 6 and 100 characters.", example = "newStrongPassword456", requiredMode = Schema.RequiredMode.REQUIRED)
        @NotBlank
        @Size(min = 6, max = 100)
        String newPassword
) {
}
