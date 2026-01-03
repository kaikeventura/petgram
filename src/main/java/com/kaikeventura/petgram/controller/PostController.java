package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.service.PostService;
import lombok.RequiredArgsConstructor;
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
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            @RequestParam("caption") String caption,
            @RequestParam(value = "taggedPetIds", required = false) List<UUID> taggedPetIds,
            @RequestParam("file") MultipartFile file
    ) {
        var post = postService.createPost(caption, taggedPetIds, file);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping("/feed")
    public ResponseEntity<Page<PostResponse>> getNewsFeed(Pageable pageable) {
        var feed = postService.getNewsFeed(pageable);
        return ResponseEntity.ok(feed);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findPostById(@PathVariable UUID postId) {
        return ResponseEntity.ok(postService.findPostById(postId));
    }
}
