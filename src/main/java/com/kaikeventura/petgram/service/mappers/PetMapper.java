package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.dto.PetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PetMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    PetResponse toPetResponse(Pet pet);
}
