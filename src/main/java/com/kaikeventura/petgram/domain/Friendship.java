package com.kaikeventura.petgram.domain;

import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "friendships")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Friendship implements Serializable {

    @EmbeddedId
    private FriendshipId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("requesterPetId")
    @JoinColumn(name = "requester_pet_id")
    private Pet requesterPet;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("addresseePetId")
    @JoinColumn(name = "addressee_pet_id")
    private Pet addresseePet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private FriendshipStatus status;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public Friendship(FriendshipId id, Pet requesterPet, Pet addresseePet, FriendshipStatus status) {
        this.id = id;
        this.requesterPet = requesterPet;
        this.addresseePet = addresseePet;
        this.status = status;
    }
}
