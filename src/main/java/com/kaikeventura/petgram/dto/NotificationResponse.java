package com.kaikeventura.petgram.dto;

import com.kaikeventura.petgram.domain.enums.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;

public record NotificationResponse(
        UUID id,
        NotificationType type,
        String message,
        String link,
        boolean isRead,
        LocalDateTime createdAt
) {
}
