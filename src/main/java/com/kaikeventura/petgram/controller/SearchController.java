package com.kaikeventura.petgram.controller;

import com.kaikeventura.petgram.dto.SearchResponse;
import com.kaikeventura.petgram.service.SearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search")
@RequiredArgsConstructor
public class SearchController {

    private final SearchService searchService;

    @GetMapping
    public ResponseEntity<SearchResponse> search(@RequestParam("query") String query) {
        return ResponseEntity.ok(searchService.search(query));
    }
}
