package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Like;
import com.kaikeventura.petgram.domain.LikeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, LikeId> {
}
