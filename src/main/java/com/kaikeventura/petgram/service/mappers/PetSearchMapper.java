package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.dto.PetSearchResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PetSearchMapper {
    PetSearchResponse toPetSearchResponse(Pet pet);
}
