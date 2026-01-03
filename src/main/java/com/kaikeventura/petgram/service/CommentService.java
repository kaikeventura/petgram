package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Comment;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.CommentRequest;
import com.kaikeventura.petgram.dto.CommentResponse;
import com.kaikeventura.petgram.event.PostCommentedEvent;
import com.kaikeventura.petgram.repository.CommentRepository;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentMapper commentMapper;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public CommentResponse createComment(UUID postId, CommentRequest commentRequest) {
        var user = getCurrentUser();
        var post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));

        var comment = new Comment(
                null,
                commentRequest.text(),
                user,
                post,
                null
        );

        var savedComment = commentRepository.save(comment);

        if (!post.getAuthor().equals(user)) {
            eventPublisher.publishEvent(new PostCommentedEvent(this, savedComment));
        }

        return commentMapper.toCommentResponse(savedComment);
    }

    @Transactional
    public void deleteComment(UUID commentId) {
        var user = getCurrentUser();
        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("Comment not found."));

        boolean isCommentAuthor = comment.getUser().equals(user);
        boolean isPostAuthor = comment.getPost().getAuthor().equals(user);

        if (!isCommentAuthor && !isPostAuthor) {
            throw new SecurityException("You are not authorized to delete this comment.");
        }

        commentRepository.delete(comment);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
