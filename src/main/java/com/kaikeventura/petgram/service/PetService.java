package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import com.kaikeventura.petgram.dto.PetRequest;
import com.kaikeventura.petgram.dto.PetResponse;
import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.repository.FriendshipRepository;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.PetMapper;
import com.kaikeventura.petgram.service.mappers.PostMapper;
import com.kaikeventura.petgram.service.mappers.S3UrlMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final PostRepository postRepository;
    private final S3StorageService storageService;
    private final PetMapper petMapper;
    private final S3UrlMapper s3UrlMapper;
    private final PostMapper postMapper;

    @Transactional
    public PetResponse createPet(PetRequest petRequest) {
        var owner = getCurrentUser();
        var pet = new Pet(
                null, // ID
                petRequest.name(),
                petRequest.breed(),
                petRequest.birthDate(),
                null, // avatarUrl
                owner,
                null, // createdAt
                null  // updatedAt
        );

        var savedPet = petRepository.save(pet);
        return petMapper.toPetResponse(savedPet);
    }

    @Transactional(readOnly = true)
    public List<PetResponse> findMyPets() {
        var owner = getCurrentUser();
        return petRepository.findByOwner(owner).stream()
                .map(petMapper::toPetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public PetResponse findPetById(UUID petId) {
        return petRepository.findById(petId)
                .map(petMapper::toPetResponse)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found."));
    }

    @Transactional
    public PetResponse updatePet(UUID petId, PetRequest petRequest) {
        var currentUser = getCurrentUser();
        var pet = findPetByIdDomain(petId);

        if (!pet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of this pet.");
        }

        pet.setName(petRequest.name());
        pet.setBreed(petRequest.breed());
        pet.setBirthDate(petRequest.birthDate());

        var updatedPet = petRepository.save(pet);
        return petMapper.toPetResponse(updatedPet);
    }

    @Transactional
    public void deletePet(UUID petId) {
        var currentUser = getCurrentUser();
        var pet = findPetByIdDomain(petId);

        if (!pet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of this pet.");
        }

        petRepository.delete(pet);
    }

    @Transactional(readOnly = true)
    public List<PetResponse> listFriends(UUID petId) {
        var pet = findPetByIdDomain(petId);

        var friendsAsRequester = friendshipRepository.findByRequesterPetAndStatus(pet, FriendshipStatus.ACCEPTED)
                .stream()
                .map(Friendship::getAddresseePet);

        var friendsAsAddressee = friendshipRepository.findByAddresseePetAndStatus(pet, FriendshipStatus.ACCEPTED)
                .stream()
                .map(Friendship::getRequesterPet);

        return Stream.concat(friendsAsRequester, friendsAsAddressee)
                .map(petMapper::toPetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponse> getFollowers(UUID petId) {
        var pet = findPetByIdDomain(petId);
        return friendshipRepository.findByAddresseePetAndStatus(pet, FriendshipStatus.ACCEPTED)
                .stream()
                .map(Friendship::getRequesterPet)
                .map(petMapper::toPetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponse> getFollowing(UUID petId) {
        var pet = findPetByIdDomain(petId);
        return friendshipRepository.findByRequesterPetAndStatus(pet, FriendshipStatus.ACCEPTED)
                .stream()
                .map(Friendship::getAddresseePet)
                .map(petMapper::toPetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<PetResponse> findPetsByOwner(UUID userId) {
        var owner = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
        return petRepository.findByOwner(owner).stream()
                .map(petMapper::toPetResponse)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> findPostsByPet(UUID petId, Pageable pageable) {
        var pet = findPetByIdDomain(petId);
        return postRepository.findByAuthorInOrderByCreatedAtDesc(List.of(pet), pageable)
                .map(post -> postMapper.toPostResponse(post, petId));
    }

    @Transactional
    public String updatePetAvatar(UUID petId, MultipartFile file) {
        var currentUser = getCurrentUser();
        var pet = findPetByIdDomain(petId);

        if (!pet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of this pet.");
        }

        var avatarKey = storageService.uploadFile(file);
        pet.setAvatarUrl(avatarKey);
        petRepository.save(pet);

        // Generate a presigned URL for the immediate response
        return s3UrlMapper.generatePresignedUrl(avatarKey);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private Pet findPetByIdDomain(UUID petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found."));
    }
}
