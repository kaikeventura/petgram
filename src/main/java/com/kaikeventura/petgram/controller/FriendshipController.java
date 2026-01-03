package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.ErrorResponse;
import com.kaikeventura.petgram.dto.FriendshipActionRequest;
import com.kaikeventura.petgram.dto.FriendshipRequestResponse;
import com.kaikeventura.petgram.dto.FriendshipStatusResponse;
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
@Tag(name = "Friendships", description = "Endpoints for managing pet friendships.")
@SecurityRequirement(name = "bearerAuth")
public class FriendshipController {

    private final FriendshipService friendshipService;

    @Operation(summary = "Send a friend request", description = "Sends a friend request from one pet to another. The current user must own the requester pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request sent successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user does not own the requester pet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "One of the pets was not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "A friendship/request already exists", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/request")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void sendFriendRequest(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.sendFriendRequest(request.requesterPetId(), request.addresseePetId());
    }

    @Operation(summary = "Accept a friend request", description = "Accepts a pending friend request. The current user must own the addressee pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Request accepted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user does not own the addressee pet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Friendship request not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "The request is not in a 'PENDING' state", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/accept")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void acceptFriendRequest(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.acceptFriendRequest(request.addresseePetId(), request.requesterPetId());
    }

    @Operation(summary = "Get pending requests for a pet", description = "Fetches a list of pending friend requests for a specific pet. The current user must own the pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved requests"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user does not own the pet", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Pet not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/requests/pending/{petId}")
    public ResponseEntity<List<FriendshipRequestResponse>> getPendingRequests(
            @Parameter(description = "The UUID of the pet to fetch pending requests for.") @PathVariable UUID petId
    ) {
        return ResponseEntity.ok(friendshipService.getPendingRequestsForPet(petId));
    }

    @Operation(summary = "Remove a friendship", description = "Removes an 'ACCEPTED' friendship between two pets. The current user must own at least one of the pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friendship removed successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user does not own one of the pets", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Friendship not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/remove")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeFriendship(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.removeFriendship(request.requesterPetId(), request.addresseePetId());
    }

    @Operation(summary = "Block a user/request", description = "Changes a 'PENDING' or 'ACCEPTED' friendship status to 'BLOCKED'. The current user must own at least one of the pets.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Friendship blocked successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user does not own one of the pets", content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Friendship not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/block")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void blockFriendship(@Valid @RequestBody FriendshipActionRequest request) {
        friendshipService.blockFriendship(request.requesterPetId(), request.addresseePetId());
    }

    @Operation(summary = "Get friendship status", description = "A utility endpoint to check the friendship status between two pets (e.g., PENDING, ACCEPTED, BLOCKED, or null if no relationship exists).")
    @GetMapping("/status")
    public ResponseEntity<FriendshipStatusResponse> getFriendshipStatus(
            @Parameter(description = "The UUID of the first pet.") @RequestParam("pet1") UUID petId1,
            @Parameter(description = "The UUID of the second pet.") @RequestParam("pet2") UUID petId2
    ) {
        return ResponseEntity.ok(friendshipService.getFriendshipStatus(petId1, petId2));
    }
}
