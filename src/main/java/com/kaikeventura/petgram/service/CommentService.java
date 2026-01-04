package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Comment;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.CommentRequest;
import com.kaikeventura.petgram.dto.CommentResponse;
import com.kaikeventura.petgram.event.PostCommentedEvent;
import com.kaikeventura.petgram.repository.CommentRepository;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.CommentMapper;
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
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentResponse createComment(UUID petId, UUID postId, CommentRequest commentRequest) {
        var user = getCurrentUser();
        var pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found."));

        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only comment as your own pet.");
        }

        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        var comment = new Comment(
                null,
                commentRequest.text(),
                pet,
                post,
                null
        );

        var savedComment = commentRepository.save(comment);

        if (!post.getAuthor().equals(pet)) {
            eventPublisher.publishEvent(new PostCommentedEvent(this, savedComment));
        }

        return commentMapper.toCommentResponse(savedComment);
    }

    @Transactional
    public void deleteComment(UUID petId, UUID commentId) {
        var user = getCurrentUser();
        var pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found."));

        if (!pet.getOwner().getId().equals(user.getId())) {
            throw new IllegalArgumentException("You can only delete comments as your own pet.");
        }

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));

        boolean isCommentAuthor = comment.getAuthor().equals(pet);
        boolean isPostAuthor = comment.getPost().getAuthor().equals(pet);

        if (!isCommentAuthor && !isPostAuthor) {
            throw new SecurityException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public Page<CommentResponse> getCommentsForPost(UUID postId, Pageable pageable) {
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
        return commentRepository.findByPostOrderByCreatedAtDesc(post, pageable)
                .map(commentMapper::toCommentResponse);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
