package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.dto.AuthorResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = S3UrlMapper.class)
public interface UserMapper {
    @Mapping(source = "avatarUrl", target = "avatarUrl", qualifiedByName = "generatePresignedUrl")
    AuthorResponse toAuthorResponse(Pet pet);
}
