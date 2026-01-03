package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.CommentRequest;
import com.kaikeventura.petgram.dto.CommentResponse;
import com.kaikeventura.petgram.dto.ErrorResponse;
import com.kaikeventura.petgram.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping
@RequiredArgsConstructor
@Tag(name = "Posts & Interactions", description = "Endpoints for creating, viewing, and interacting with posts.")
@SecurityRequirement(name = "bearerAuth")
public class CommentController {

    private final CommentService commentService;

    @Operation(summary = "Add a comment to a post", description = "Creates a new comment on a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @Parameter(description = "The UUID of the post to comment on.") @PathVariable UUID postId,
            @Valid @RequestBody CommentRequest commentRequest
    ) {
        var createdComment = commentService.createComment(postId, commentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdComment);
    }

    @Operation(summary = "Delete a comment", description = "Deletes a comment. The current user must be the author of the comment or the author of the post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Comment deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user is not authorized to delete the comment", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Comment not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/comments/{commentId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteComment(@Parameter(description = "The UUID of the comment to delete.") @PathVariable UUID commentId) {
        commentService.deleteComment(commentId);
    }

    @Operation(summary = "List comments for a post", description = "Fetches a paginated list of comments for a specific post.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved comments"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<Page<CommentResponse>> getCommentsForPost(
            @Parameter(description = "The UUID of the post whose comments are to be fetched.") @PathVariable UUID postId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(commentService.getCommentsForPost(postId, pageable));
    }
}
