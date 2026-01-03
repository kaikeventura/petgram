package com.kaikeventura.petgram.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class FriendshipId implements Serializable {
    @Column(name = "requester_id")
    private UUID requesterId;

    @Column(name = "addressee_id")
    private UUID addresseeId;
}
