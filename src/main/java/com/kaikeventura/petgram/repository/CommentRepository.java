package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Comment;
import com.kaikeventura.petgram.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {
    Page<Comment> findByPostOrderByCreatedAtDesc(Post post, Pageable pageable);
}
