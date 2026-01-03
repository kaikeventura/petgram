package com.kaikeventura.petgram.domain;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
public class LikeId implements Serializable {
    private UUID userId;
    private UUID postId;
}
