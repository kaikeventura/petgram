package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.SearchResponse;
import com.kaikeventura.petgram.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
@Tag(name = "Search", description = "Endpoints for content discovery.")
@SecurityRequirement(name = "bearerAuth")
public class SearchController {

    private final SearchService searchService;

    @Operation(summary = "Search for users and pets", description = "Performs a simple, case-insensitive search for users and pets by name.")
    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam("query") String query) {
        return ResponseEntity.ok(searchService.search(query));
    }
}
