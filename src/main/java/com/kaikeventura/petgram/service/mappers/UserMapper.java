package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.dto.AuthorResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    AuthorResponse toAuthorResponse(Pet pet);
}
