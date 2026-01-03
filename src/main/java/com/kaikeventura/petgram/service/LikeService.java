package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Like;
import com.kaikeventura.petgram.domain.LikeId;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.event.PostLikedEvent;
import com.kaikeventura.petgram.repository.LikeRepository;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void likePost(UUID postId) {
        var user = getCurrentUser();
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        var likeId = new LikeId(user.getId(), postId);

        if (likeRepository.existsById(likeId)) {
            throw new IllegalStateException("You have already liked this post.");
        }

        var like = new Like(likeId, user, post, null);
        likeRepository.save(like);

        // Publish event only if the like is not from the post author
        if (!post.getAuthor().equals(user)) {
            eventPublisher.publishEvent(new PostLikedEvent(this, like));
        }
    }

    @Transactional
    public void unlikePost(UUID postId) {
        var user = getCurrentUser();
        var likeId = new LikeId(user.getId(), postId);

        if (!likeRepository.existsById(likeId)) {
            throw new IllegalStateException("You have not liked this post.");
        }

        likeRepository.deleteById(likeId);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
