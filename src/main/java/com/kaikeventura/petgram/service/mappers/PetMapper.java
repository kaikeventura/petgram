package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.dto.PetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = S3UrlMapper.class)
public abstract class PetMapper {

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "avatarUrl", target = "avatarUrl", qualifiedByName = "generatePresignedUrl")
    public abstract PetResponse toPetResponse(Pet pet);
}
