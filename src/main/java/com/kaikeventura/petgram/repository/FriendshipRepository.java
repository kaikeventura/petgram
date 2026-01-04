package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.FriendshipId;
import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    @Query("""
        SELECT f FROM Friendship f
        WHERE (f.requesterPet.id = :petId1 AND f.addresseePet.id = :petId2)
           OR (f.requesterPet.id = :petId2 AND f.addresseePet.id = :petId1)
    """)
    Optional<Friendship> findFriendshipBetweenPets(@Param("petId1") UUID petId1, @Param("petId2") UUID petId2);

    List<Friendship> findByAddresseePetAndStatus(Pet addresseePet, FriendshipStatus status);

    List<Friendship> findByRequesterPetAndStatus(Pet requesterPet, FriendshipStatus status);

    long countByAddresseePetAndStatus(Pet addresseePet, FriendshipStatus status);

    long countByRequesterPetAndStatus(Pet requesterPet, FriendshipStatus status);
}
