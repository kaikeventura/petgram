package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.dto.SearchResponse;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.PetSearchMapper;
import com.kaikeventura.petgram.service.mappers.UserSearchMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SearchService {

    private static final int SEARCH_RESULT_LIMIT = 10;

    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final UserSearchMapper userSearchMapper;
    private final PetSearchMapper petSearchMapper;

    @Transactional(readOnly = true)
    public SearchResponse search(String query) {
        var pageable = PageRequest.of(0, SEARCH_RESULT_LIMIT);

        var users = userRepository.findByNameContainingIgnoreCase(query, pageable).stream()
                .map(userSearchMapper::toUserSearchResponse)
                .collect(Collectors.toList());

        var pets = petRepository.findByNameContainingIgnoreCase(query, pageable).stream()
                .map(petSearchMapper::toPetSearchResponse)
                .collect(Collectors.toList());

        return new SearchResponse(users, pets);
    }
}
