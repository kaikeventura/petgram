package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import com.kaikeventura.petgram.dto.PetResponse;
import com.kaikeventura.petgram.repository.FriendshipRepository;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.beans.factory.annotation.Autowired;

@Mapper(componentModel = "spring", uses = S3UrlMapper.class)
public abstract class PetMapper {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "avatarUrl", target = "avatarUrl", qualifiedByName = "generatePresignedUrl")
    @Mapping(source = "pet", target = "followerCount", qualifiedByName = "mapFollowers")
    @Mapping(source = "pet", target = "followingCount", qualifiedByName = "mapFollowing")
    public abstract PetResponse toPetResponse(Pet pet);

    @Named("mapFollowers")
    long mapFollowers(Pet pet) {
        return friendshipRepository.countByAddresseePetAndStatus(pet, FriendshipStatus.ACCEPTED);
    }

    @Named("mapFollowing")
    long mapFollowing(Pet pet) {
        return friendshipRepository.countByRequesterPetAndStatus(pet, FriendshipStatus.ACCEPTED);
    }
}
