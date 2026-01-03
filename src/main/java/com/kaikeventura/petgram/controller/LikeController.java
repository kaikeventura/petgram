package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.LikeResponse;
import com.kaikeventura.petgram.service.LikeService;
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
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@PathVariable UUID postId) {
        likeService.likePost(postId);
    }

    @DeleteMapping("/like")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikePost(@PathVariable UUID postId) {
        likeService.unlikePost(postId);
    }

    @GetMapping("/likes")
    public ResponseEntity<Page<LikeResponse>> getLikesForPost(@PathVariable UUID postId, Pageable pageable) {
        return ResponseEntity.ok(likeService.getLikesForPost(postId, pageable));
    }
}
