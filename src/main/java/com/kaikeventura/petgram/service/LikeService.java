package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Like;
import com.kaikeventura.petgram.domain.LikeId;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.LikeResponse;
import com.kaikeventura.petgram.event.PostLikedEvent;
import com.kaikeventura.petgram.repository.LikeRepository;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.LikeMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final LikeMapper likeMapper;

    @Transactional
    public void likePost(UUID petId, UUID postId) {
        var user = getCurrentUser();
        var pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found."));

        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only like posts as your own pet.");
        }

        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        var likeId = new LikeId(petId, postId);

        if (likeRepository.existsById(likeId)) {
            throw new IllegalStateException("You have already liked this post.");
        }

        var like = new Like(likeId, pet, post, null);
        likeRepository.save(like);

        // Publish event only if the like is not from the post author
        if (!post.getAuthor().equals(pet)) {
            eventPublisher.publishEvent(new PostLikedEvent(this, like));
        }
    }

    @Transactional
    public void unlikePost(UUID petId, UUID postId) {
        var user = getCurrentUser();
        var pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found."));

        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only unlike posts as your own pet.");
        }

        var likeId = new LikeId(petId, postId);

        if (!likeRepository.existsById(likeId)) {
            throw new IllegalStateException("You have not liked this post.");
        }

        likeRepository.deleteById(likeId);
    }

    @Transactional(readOnly = true)
    public Page<LikeResponse> getLikesForPost(UUID postId, Pageable pageable) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
        return likeRepository.findByPost(post, pageable)
                .map(likeMapper::toLikeResponse);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
