package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Like;
import com.kaikeventura.petgram.domain.LikeId;
import com.kaikeventura.petgram.domain.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
    Page<Like> findByPost(Post post, Pageable pageable);
}
