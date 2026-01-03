package com.kaikeventura.petgram.dto;

import java.util.List;

public record SearchResponse(
        List<UserSearchResponse> users,
        List<PetSearchResponse> pets
) {
}
