package com.kaikeventura.petgram.dto;

import com.kaikeventura.petgram.domain.enums.FriendshipStatus;

public record FriendshipStatusResponse(
        FriendshipStatus status
) {
}
