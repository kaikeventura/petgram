package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    /**
     * Fetches the news feed for a given pet, based on pet friendships.
     * The feed includes posts from:
     * 1. The pet themselves.
     * 2. The pets that are friends with the given pet.
     */
    @Query(value = """
        SELECT p FROM Post p WHERE p.author.id = :petId OR p.author.id IN (
            SELECT f.addresseePet.id FROM Friendship f
            WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.requesterPet.id = :petId
            UNION
            SELECT f.requesterPet.id FROM Friendship f
            WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.addresseePet.id = :petId
        )
        ORDER BY p.createdAt DESC
    """,
    countQuery = """
        SELECT count(p) FROM Post p WHERE p.author.id = :petId OR p.author.id IN (
            SELECT f.addresseePet.id FROM Friendship f
            WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.requesterPet.id = :petId
            UNION
            SELECT f.requesterPet.id FROM Friendship f
            WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.addresseePet.id = :petId
        )
    """)
    Page<Post> findNewsFeedForPet(@Param("petId") UUID petId, Pageable pageable);

    Page<Post> findByAuthorInOrderByCreatedAtDesc(List<Pet> authors, Pageable pageable);
}
