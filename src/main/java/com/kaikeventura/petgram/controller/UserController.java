package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.*;
import com.kaikeventura.petgram.service.PetService;
import com.kaikeventura.petgram.service.UserService;
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
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Users & Profiles", description = "Endpoints for managing and viewing user profiles and their content.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;
    private final PetService petService;

    @Operation(summary = "Get current user's profile", description = "Fetches the profile information of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @Operation(summary = "Get current user's posts", description = "Fetches a paginated list of posts created by the currently authenticated user.")
    @GetMapping("/me/posts")
    public ResponseEntity<Page<PostResponse>> getCurrentUserPosts(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(userService.getCurrentUserPosts(pageable));
    }

    @Operation(summary = "Update current user's profile", description = "Updates the name of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/me")
    public ResponseEntity<UserProfileResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateCurrentUser(userUpdateRequest));
    }

    @Operation(summary = "Update current user's password", description = "Updates the password of the currently authenticated user. Requires the old password for verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect old password or invalid new password", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/me/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUserPassword(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        userService.updateCurrentUserPassword(passwordUpdateRequest);
    }

    @Operation(summary = "Delete current user's account", description = "Deletes the account and all associated data of the currently authenticated user. This action is irreversible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully")
    })
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser() {
        userService.deleteCurrentUser();
    }

    @Operation(summary = "Get a user's public profile", description = "Fetches the public profile information of a user by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}")
    public ResponseEntity<UserProfileResponse> getUserProfile(
            @Parameter(description = "The UUID of the user to fetch.") @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(userService.getUserProfile(userId));
    }

    @Operation(summary = "Get a user's posts", description = "Fetches a paginated list of posts created by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}/posts")
    public ResponseEntity<Page<PostResponse>> getUserPosts(
            @Parameter(description = "The UUID of the user whose posts are to be fetched.") @PathVariable UUID userId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(userService.getUserPosts(userId, pageable));
    }

    @Operation(summary = "Get a user's pets", description = "Fetches a list of pets owned by a specific user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pets"),
            @ApiResponse(responseCode = "404", description = "User not found", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/{userId}/pets")
    public ResponseEntity<List<PetResponse>> getPetsByOwner(
            @Parameter(description = "The UUID of the user whose pets are to be fetched.") @PathVariable UUID userId
    ) {
        return ResponseEntity.ok(petService.findPetsByOwner(userId));
    }
}
