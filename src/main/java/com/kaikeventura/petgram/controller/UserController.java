package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.dto.PetResponse;
import com.kaikeventura.petgram.dto.UserProfileResponse;
import com.kaikeventura.petgram.dto.UserUpdateRequest;
import com.kaikeventura.petgram.service.PetService;
import com.kaikeventura.petgram.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final PetService petService;

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

    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(@PathVariable UUID userId) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostResponse>> getUserPosts(@PathVariable UUID userId, Pageable pageable) {
        return ResponseEntity.ok(userService.getUserPosts(userId, pageable));
    }

    @GetMapping("/{userId}/pets")
    public ResponseEntity<List<PetResponse>> getPetsByOwner(@PathVariable UUID userId) {
        return ResponseEntity.ok(petService.findPetsByOwner(userId));
    }
}
