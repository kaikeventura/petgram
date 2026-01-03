package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<Post, UUID> {

    /**
     * Fetches the news feed for a given user, based on pet friendships.
     * The feed includes posts from:
     * 1. The user themselves.
     * 2. The owners of pets that are friends with the user's own pets.
     */
    @Query(value = """
        SELECT p FROM Post p WHERE p.author.id = :userId OR p.author.id IN (
            SELECT pet.owner.id FROM Pet pet WHERE pet.id IN (
                SELECT f.addresseePet.id FROM Friendship f
                WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.requesterPet.owner.id = :userId
                UNION
                SELECT f.requesterPet.id FROM Friendship f
                WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.addresseePet.owner.id = :userId
            )
        )
        ORDER BY p.createdAt DESC
    """,
    countQuery = """
        SELECT count(p) FROM Post p WHERE p.author.id = :userId OR p.author.id IN (
            SELECT DISTINCT pet.owner.id FROM Pet pet WHERE pet.id IN (
                SELECT f.addresseePet.id FROM Friendship f
                WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.requesterPet.owner.id = :userId
                UNION
                SELECT f.requesterPet.id FROM Friendship f
                WHERE f.status = com.kaikeventura.petgram.domain.enums.FriendshipStatus.ACCEPTED AND f.addresseePet.owner.id = :userId
            )
        )
    """)
    Page<Post> findNewsFeedForUser(@Param("userId") UUID userId, Pageable pageable);
}
