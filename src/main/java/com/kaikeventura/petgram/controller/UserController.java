package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.dto.UserProfileResponse;
import com.kaikeventura.petgram.dto.UserUpdateRequest;
import com.kaikeventura.petgram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @GetMapping("/me/posts")
    public ResponseEntity<Page<PostResponse>> getCurrentUserPosts(Pageable pageable) {
        return ResponseEntity.ok(userService.getCurrentUserPosts(pageable));
    }

    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateCurrentUser(userUpdateRequest));
    }
}
