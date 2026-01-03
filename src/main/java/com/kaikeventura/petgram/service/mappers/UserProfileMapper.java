package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.UserProfileResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserProfileMapper {
    UserProfileResponse toUserProfileResponse(User user);
}
