package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.LikeResponse;
import com.kaikeventura.petgram.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts/{postId}")
@RequiredArgsConstructor
@Tag(name = "Posts & Interactions", description = "Endpoints for creating, viewing, and interacting with posts.")
@SecurityRequirement(name = "bearerAuth")
public class LikeController {

    private final LikeService likeService;

    @Operation(summary = "Like a post", description = "Adds a 'like' from the current user to a post. Fails if the post is already liked.")
    @PostMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@PathVariable UUID postId) {
        likeService.likePost(postId);
    }

    @Operation(summary = "Unlike a post", description = "Removes the current user's 'like' from a post.")
    @DeleteMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikePost(@PathVariable UUID postId) {
        likeService.unlikePost(postId);
    }

    @Operation(summary = "List users who liked a post", description = "Fetches a paginated list of users who have liked a specific post.")
    @GetMapping("/likes")
    public ResponseEntity<Page<LikeResponse>> getLikesForPost(@PathVariable UUID postId, Pageable pageable) {
        return ResponseEntity.ok(likeService.getLikesForPost(postId, pageable));
    }
}
