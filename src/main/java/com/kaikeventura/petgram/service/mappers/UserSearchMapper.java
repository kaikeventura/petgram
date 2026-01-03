package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.UserSearchResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserSearchMapper {
    UserSearchResponse toUserSearchResponse(User user);
}
