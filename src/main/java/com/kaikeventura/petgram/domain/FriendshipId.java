package com.kaikeventura.petgram.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class FriendshipId implements Serializable {
    private UUID requesterId;
    private UUID addresseeId;
}
