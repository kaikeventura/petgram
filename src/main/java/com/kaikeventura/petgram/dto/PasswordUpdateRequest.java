package com.kaikeventura.petgram.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PasswordUpdateRequest(
        @NotBlank
        String oldPassword,

        @NotBlank
        @Size(min = 6, max = 100)
        String newPassword
) {
}
