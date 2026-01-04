package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.*;
import com.kaikeventura.petgram.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
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

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "Endpoints for managing the authenticated user's account.")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get current user's profile", description = "Fetches the profile information of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user profile"),
            @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<UserProfileResponse> getCurrentUserProfile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }

    @Operation(summary = "Update current user's profile", description = "Updates the name of the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Profile updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping
    public ResponseEntity<UserProfileResponse> updateCurrentUser(@Valid @RequestBody UserUpdateRequest userUpdateRequest) {
        return ResponseEntity.ok(userService.updateCurrentUser(userUpdateRequest));
    }

    @Operation(summary = "Update current user's password", description = "Updates the password of the currently authenticated user. Requires the old password for verification.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Password updated successfully"),
            @ApiResponse(responseCode = "400", description = "Incorrect old password or invalid new password", content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PutMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateCurrentUserPassword(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        userService.updateCurrentUserPassword(passwordUpdateRequest);
    }

    @Operation(summary = "Delete current user's account", description = "Deletes the account and all associated data of the currently authenticated user. This action is irreversible.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Account deleted successfully")
    })
    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCurrentUser() {
        userService.deleteCurrentUser();
    }
}
