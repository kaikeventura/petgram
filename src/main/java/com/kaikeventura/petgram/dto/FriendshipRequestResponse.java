package com.kaikeventura.petgram.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record FriendshipRequestResponse(
        UUID requesterId,
        String requesterName,
        LocalDateTime requestedAt
) {
}
