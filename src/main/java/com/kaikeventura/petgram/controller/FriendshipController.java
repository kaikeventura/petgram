package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.FriendshipActionRequest;
import com.kaikeventura.petgram.dto.FriendshipRequestResponse;
import com.kaikeventura.petgram.dto.FriendshipStatusResponse;
import com.kaikeventura.petgram.service.FriendshipService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
public class FriendshipController {

    private final FriendshipService friendshipService;

    @PostMapping("/request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendFriendRequest(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.sendFriendRequest(request.requesterPetId(), request.addresseePetId());
    }

    @PostMapping("/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptFriendRequest(@Valid @RequestBody FriendshipActionRequest request) {
        // For accepting, the addressee is the one taking action.
        friendshipService.acceptFriendRequest(request.addresseePetId(), request.requesterPetId());
    }

    @GetMapping("/requests/pending/{petId}")
    public ResponseEntity<List<FriendshipRequestResponse>> getPendingRequests(@PathVariable UUID petId) {
        return ResponseEntity.ok(friendshipService.getPendingRequestsForPet(petId));
    }

    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriendship(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.removeFriendship(request.requesterPetId(), request.addresseePetId());
    }

    @PostMapping("/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockFriendship(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.blockFriendship(request.requesterPetId(), request.addresseePetId());
    }

    @GetMapping("/status")
    public ResponseEntity<FriendshipStatusResponse> getFriendshipStatus(
            @RequestParam("pet1") UUID petId1,
            @RequestParam("pet2") UUID petId2
    ) {
        return ResponseEntity.ok(friendshipService.getFriendshipStatus(petId1, petId2));
    }
}
