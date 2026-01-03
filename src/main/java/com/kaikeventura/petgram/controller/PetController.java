package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.PetRequest;
import com.kaikeventura.petgram.dto.PetResponse;
import com.kaikeventura.petgram.service.PetService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pets")
@RequiredArgsConstructor
public class PetController {

    private final PetService petService;

    @PostMapping
    public ResponseEntity<PetResponse> createPet(@Valid @RequestBody PetRequest petRequest) {
        var createdPet = petService.createPet(petRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPet);
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> findMyPets() {
        return ResponseEntity.ok(petService.findMyPets());
    }
}
