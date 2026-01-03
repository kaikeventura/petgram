package com.kaikeventura.petgram.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class LikeId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;

    @Column(name = "post_id")
    private UUID postId;
}
