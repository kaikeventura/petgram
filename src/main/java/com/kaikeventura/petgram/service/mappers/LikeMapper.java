package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Like;
import com.kaikeventura.petgram.dto.LikeResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = S3UrlMapper.class)
public interface LikeMapper {

    @Mapping(source = "pet.id", target = "petId")
    @Mapping(source = "pet.name", target = "petName")
    @Mapping(source = "pet.avatarUrl", target = "avatarUrl", qualifiedByName = "generatePresignedUrl")
    LikeResponse toLikeResponse(Like like);
}
