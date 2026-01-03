package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.ErrorResponse;
import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.service.PostService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
@Tag(name = "Posts & Interactions", description = "Endpoints for creating, viewing, and interacting with posts.")
@SecurityRequirement(name = "bearerAuth")
public class PostController {

    private final PostService postService;

    @Operation(summary = "Create a new post", description = "Creates a new post with an image, a caption, and optionally tags a list of pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input, e.g., one of the tagged pets was not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @Parameter(description = "The caption for the post.") @RequestParam("caption") String caption,
            @Parameter(description = "A list of pet UUIDs to be tagged in the post.") @RequestParam(value = "taggedPetIds", required = false) List<UUID> taggedPetIds,
            @Parameter(description = "The image file for the post.") @RequestParam("file") MultipartFile file
    ) {
        var post = postService.createPost(caption, taggedPetIds, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @Operation(summary = "Get the news feed", description = "Fetches the paginated news feed for the authenticated user, containing posts from the user and their friends' owners.")
    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponse>> getNewsFeed(@ParameterObject Pageable pageable) {
        var feed = postService.getNewsFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    @Operation(summary = "Get a specific post", description = "Fetches the details of a single post by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved post"),
            @ApiResponse(responseCode = "404", description = "Post not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPostById(
            @Parameter(description = "The UUID of the post to fetch.") @PathVariable UUID postId
    ) {
        return ResponseEntity.ok(postService.findPostById(postId));
    }
}
