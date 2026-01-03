package com.kaikeventura.petgram.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FriendshipId implements Serializable {
    @Column(name = "requester_id")
    private UUID requesterId;

    @Column(name = "addressee_id")
    private UUID addresseeId;
}
