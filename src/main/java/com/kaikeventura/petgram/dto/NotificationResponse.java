package com.kaikeventura.petgram.dto;

import com.kaikeventura.petgram.domain.enums.NotificationType;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Represents a notification for the user.")
public record NotificationResponse(
        @Schema(description = "The unique identifier of the notification.")
        UUID id,

        @Schema(description = "The type of the notification (e.g., POST_LIKE, FRIENDSHIP_REQUEST).")
        NotificationType type,

        @Schema(description = "The human-readable message for the notification.", example = "Rex liked your post.")
        String message,

        @Schema(description = "A URL link to the relevant content (e.g., the post that was liked).", example = "/posts/a1b2c3d4-...")
        String link,

        @Schema(description = "Indicates if the notification has been read by the user.")
        boolean isRead,

        @Schema(description = "The timestamp when the notification was created.")
        LocalDateTime createdAt
) {
}
