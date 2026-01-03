package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/posts/{postId}/like")
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void likePost(@PathVariable UUID postId) {
        likeService.likePost(postId);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unlikePost(@PathVariable UUID postId) {
        likeService.unlikePost(postId);
    }
}
