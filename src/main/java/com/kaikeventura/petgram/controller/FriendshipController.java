package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.*;
import com.kaikeventura.petgram.service.FriendshipService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/friendships")
@RequiredArgsConstructor
@Tag(name = "Friendships", description = "Endpoints for managing pet follow relationships.")
@SecurityRequirement(name = "bearerAuth")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Operation(summary = "Send a follow request", description = "Sends a follow request from the current pet to a target pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request sent successfully"),
            @ApiResponse(responseCode = "409", description = "A follow request already exists or has been established", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendFollowRequest(
            @Parameter(description = "The ID of the pet sending the request.") @RequestHeader("X-Pet-Id") UUID requesterPetId,
            @Valid @RequestBody FollowRequest request
    ) {
        friendshipService.sendFollowRequest(requesterPetId, request.targetPetId());
    }

    @Operation(summary = "Accept a follow request", description = "Accepts a pending follow request. The current pet must be the target of the request.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request accepted successfully"),
            @ApiResponse(responseCode = "404", description = "Follow request not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptFollowRequest(
            @Parameter(description = "The ID of the pet accepting the request.") @RequestHeader("X-Pet-Id") UUID targetPetId,
            @Valid @RequestBody FollowerRequest request
    ) {
        friendshipService.acceptFollowRequest(targetPetId, request.requesterPetId());
    }

    @Operation(summary = "Unfollow a pet", description = "Removes the follow relationship from the current pet to the target pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Unfollowed successfully"),
            @ApiResponse(responseCode = "404", description = "You are not following this pet", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @DeleteMapping("/unfollow/{targetPetId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void unfollow(
            @Parameter(description = "The ID of the pet initiating the unfollow.") @RequestHeader("X-Pet-Id") UUID requesterPetId,
            @Parameter(description = "The ID of the pet to unfollow.") @PathVariable UUID targetPetId
    ) {
        friendshipService.unfollow(requesterPetId, targetPetId);
    }

    @Operation(summary = "Get pending follow requests", description = "Fetches a list of pending follow requests for the current pet.")
    @GetMapping("/requests/pending")
    public ResponseEntity<List<FriendshipRequestResponse>> getPendingRequests(
            @Parameter(description = "The ID of the pet to fetch pending requests for.") @RequestHeader("X-Pet-Id") UUID petId
    ) {
        return ResponseEntity.ok(friendshipService.getPendingRequests(petId));
    }

    @Operation(summary = "Get relationship status", description = "Crucial endpoint to determine what UI to show (e.g., Follow, Following, Follow Back).")
    @GetMapping("/status/{targetPetId}")
    public ResponseEntity<RelationshipStatusResponse> getRelationshipStatus(
            @Parameter(description = "The ID of the pet viewing the profile.") @RequestHeader("X-Pet-Id") UUID currentPetId,
            @Parameter(description = "The ID of the pet whose profile is being viewed.") @PathVariable UUID targetPetId
    ) {
        return ResponseEntity.ok(friendshipService.getRelationshipStatus(currentPetId, targetPetId));
    }
}
