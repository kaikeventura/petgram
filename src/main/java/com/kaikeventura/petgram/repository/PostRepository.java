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
     * Fetches the news feed for a given user.
     * The feed includes posts from the user themselves and their accepted friends.
     *
     * The query works as follows:
     * 1. It selects all posts 'p'.
     * 2. The WHERE clause filters these posts based on the author.
     * 3. 'p.author.id = :userId' includes the user's own posts.
     * 4. The subquery identifies the user's friends:
     *    - It looks in the Friendship table for relationships where the current user is either the requester or the addressee.
     *    - It checks if the friendship status is 'ACCEPTED'.
     *    - It returns the ID of the friend (either addressee or requester, depending on the side of the relationship).
     * 5. 'p.author.id IN (...) combines both conditions, fetching posts from the user and their friends.
     * 6. The results are ordered by creation date in descending order.
     */
    @Query("""
        SELECT p FROM Post p WHERE p.author.id = :userId OR p.author.id IN (
            SELECT f.addressee.id FROM Friendship f WHERE f.requester.id = :userId AND f.status = 'ACCEPTED'
            UNION
            SELECT f.requester.id FROM Friendship f WHERE f.addressee.id = :userId AND f.status = 'ACCEPTED'
        )
    """)
    Page<Post> findNewsFeedForUser(@Param("userId") UUID userId, Pageable pageable);
}
