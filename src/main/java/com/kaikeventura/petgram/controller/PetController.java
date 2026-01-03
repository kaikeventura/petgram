package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.PetRequest;
import com.kaikeventura.petgram.dto.PetResponse;
import com.kaikeventura.petgram.service.PetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
@Tag(name = "Pets", description = "Endpoints for managing pet profiles.")
@SecurityRequirement(name = "bearerAuth")
public class PetController {

    private final PetService petService;

    @Operation(summary = "Create a new pet profile", description = "Creates a new pet profile for the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pet profile created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetRequest petRequest) {
        var createdPet = petService.createPet(petRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @Operation(summary = "List current user's pets", description = "Fetches a list of all pets owned by the currently authenticated user.")
    @GetMapping
    public ResponseEntity<List<PetResponse>> findMyPets() {
        return ResponseEntity.ok(petService.findMyPets());
    }

    @Operation(summary = "Get a pet's public profile", description = "Fetches the public profile of a pet by its ID.")
    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> findPetById(@PathVariable UUID petId) {
        return ResponseEntity.ok(petService.findPetById(petId));
    }

    @Operation(summary = "Update a pet's profile", description = "Updates the profile of a pet. The current user must be the owner of the pet.")
    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> updatePet(@PathVariable UUID petId, @Valid @RequestBody PetRequest petRequest) {
        var updatedPet = petService.updatePet(petId, petRequest);
        return ResponseEntity.ok(updatedPet);
    }

    @Operation(summary = "Delete a pet's profile", description = "Deletes a pet's profile. The current user must be the owner of the pet.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pet profile deleted successfully"),
            @ApiResponse(responseCode = "403", description = "Forbidden if the user is not the owner")
    })
    @DeleteMapping("/{petId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePet(@PathVariable UUID petId) {
        petService.deletePet(petId);
    }

    @Operation(summary = "List a pet's friends", description = "Fetches a list of all pets that are friends with the specified pet.")
    @GetMapping("/{petId}/friends")
    public ResponseEntity<List<PetResponse>> listFriends(@PathVariable UUID petId) {
        return ResponseEntity.ok(petService.listFriends(petId));
    }

    @Operation(summary = "Upload a pet's avatar", description = "Uploads a new avatar image for a pet. The current user must be the owner.")
    @PostMapping("/{petId}/avatar")
    public ResponseEntity<Map<String, String>> uploadPetAvatar(@PathVariable UUID petId, @RequestParam("file") MultipartFile file) {
        var avatarUrl = petService.updatePetAvatar(petId, file);
        return ResponseEntity.ok(Map.of("avatarUrl", avatarUrl));
    }
}
