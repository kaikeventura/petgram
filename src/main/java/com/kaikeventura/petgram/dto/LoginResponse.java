package com.kaikeventura.petgram.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Response body containing the JWT access token.")
public record LoginResponse(
        @Schema(description = "The JWT access token.", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c")
        String accessToken
) {
}
