package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.PetRequest;
import com.kaikeventura.petgram.dto.PetResponse;
import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Endpoints for viewing and managing pet profiles and their content.")
public class PetController {

    private final PetService petService;

    @Operation(summary = "Create a new pet", description = "Creates a new pet profile for the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet created successfully")
    })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @SecurityRequirement(name = "bearerAuth")
    public PetResponse createPet(@Valid @RequestBody PetRequest petRequest) {
        return petService.createPet(petRequest);
    }

    @Operation(summary = "Get the current user's pets", description = "Fetches a list of pets owned by the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pets")
    })
    @GetMapping("/my-pets")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PetResponse>> getMyPets() {
        return ResponseEntity.ok(petService.findMyPets());
    }

    @Operation(summary = "Get a pet's public profile", description = "Fetches the public profile information of a pet by their ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved pet profile"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> getPetProfile(
            @Parameter(description = "The UUID of the pet to fetch.") @PathVariable UUID petId
    ) {
        return ResponseEntity.ok(petService.findPetById(petId));
    }

    @Operation(summary = "Get a pet's posts", description = "Fetches a paginated list of posts created by a specific pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved posts"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @GetMapping("/{petId}/posts")
    public ResponseEntity<Page<PostResponse>> getPetPosts(
            @Parameter(description = "The UUID of the pet whose posts are to be fetched.") @PathVariable UUID petId,
            @ParameterObject Pageable pageable
    ) {
        return ResponseEntity.ok(petService.findPostsByPet(petId, pageable));
    }

    @Operation(summary = "Update a pet's profile", description = "Updates the profile of a pet. The current user must be the owner of the pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pet updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user is not the owner"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @PutMapping("/{petId}")
    @SecurityRequirement(name = "bearerAuth")
    public PetResponse updatePet(
            @Parameter(description = "The UUID of the pet to update.") @PathVariable UUID petId,
            @Valid @RequestBody PetRequest petRequest
    ) {
        return petService.updatePet(petId, petRequest);
    }

    @Operation(summary = "Update a pet's avatar", description = "Updates the avatar image of a pet. The current user must be the owner of the pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Avatar updated successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user is not the owner"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @PutMapping("/{petId}/avatar")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<String> updatePetAvatar(
            @Parameter(description = "The UUID of the pet to update.") @PathVariable UUID petId,
            @Parameter(description = "The new avatar image file.") @RequestParam("file") MultipartFile file
    ) {
        var presignedUrl = petService.updatePetAvatar(petId, file);
        return ResponseEntity.ok(presignedUrl);
    }

    @Operation(summary = "Delete a pet", description = "Deletes a pet's profile. The current user must be the owner of the pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user is not the owner"),
            @ApiResponse(responseCode = "404", description = "Pet not found")
    })
    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @SecurityRequirement(name = "bearerAuth")
    public void deletePet(@Parameter(description = "The UUID of the pet to delete.") @PathVariable UUID petId) {
        petService.deletePet(petId);
    }
}
