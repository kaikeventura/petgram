package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.FriendshipRequestResponse;
import com.kaikeventura.petgram.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/requests/pending")
    public ResponseEntity<List<FriendshipRequestResponse>> getPendingRequests() {
        return ResponseEntity.ok(friendshipService.getPendingRequests());
    }
}
