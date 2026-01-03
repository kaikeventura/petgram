package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.ErrorResponse;
import com.kaikeventura.petgram.dto.LikeResponse;
import com.kaikeventura.petgram.service.LikeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post liked successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Conflict, user has already liked this post", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@Parameter(description = "The UUID of the post to like.") @PathVariable UUID postId) {
        likeService.likePost(postId);
    }

    @Operation(summary = "Unlike a post", description = "Removes the current user's 'like' from a post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Post unliked successfully"),
            @ApiResponse(responseCode = "409", description = "Conflict, user has not liked this post", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikePost(@Parameter(description = "The UUID of the post to unlike.") @PathVariable UUID postId) {
        likeService.unlikePost(postId);
    }

    @Operation(summary = "List users who liked a post", description = "Fetches a paginated list of users who have liked a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of likes"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/likes")
    public ResponseEntity<Page<LikeResponse>> getLikesForPost(
            @Parameter(description = "The UUID of the post whose likes are to be fetched.") @PathVariable UUID postId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(likeService.getLikesForPost(postId, pageable));
    }
}
