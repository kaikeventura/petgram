package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request/{addresseeId}")
    public ResponseEntity<Void> sendFriendRequest(@PathVariable UUID addresseeId) {
        friendshipService.sendFriendRequest(addresseeId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/accept/{requesterId}")
    public ResponseEntity<Void> acceptFriendRequest(@PathVariable UUID requesterId) {
        friendshipService.acceptFriendRequest(requesterId);
        return ResponseEntity.ok().build();
    }
}
