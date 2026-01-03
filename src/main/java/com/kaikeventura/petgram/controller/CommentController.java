package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.CommentRequest;
import com.kaikeventura.petgram.dto.CommentResponse;
import com.kaikeventura.petgram.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Posts & Interactions", description = "Endpoints for creating, viewing, and interacting with posts.")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add a comment to a post", description = "Creates a new comment on a specific post.")
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable UUID postId,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        var createdComment = commentService.createComment(postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment. The current user must be the author of the comment or the author of the post.")
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }

    @Operation(summary = "List comments for a post", description = "Fetches a paginated list of comments for a specific post.")
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentResponse>> getCommentsForPost(
            @PathVariable UUID postId,
            Pageable pageable
    ) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId, pageable));
    }
}
