package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.FriendshipId;
import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import com.kaikeventura.petgram.dto.FriendshipRequestResponse;
import com.kaikeventura.petgram.dto.FriendshipStatusResponse;
import com.kaikeventura.petgram.repository.FriendshipRepository;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;

    @Transactional
    public void sendFriendRequest(UUID requesterPetId, UUID addresseePetId) {
        var currentUser = getCurrentUser();
        var requesterPet = findPetById(requesterPetId);

        if (!requesterPet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of the pet sending the request.");
        }

        if (requesterPetId.equals(addresseePetId)) {
            throw new IllegalArgumentException("A pet cannot send a friend request to itself.");
        }

        var addresseePet = findPetById(addresseePetId);

        friendshipRepository.findFriendshipBetweenPets(requesterPetId, addresseePetId)
                .ifPresent(friendship -> {
                    throw new IllegalStateException("A friendship request already exists or has been established between these pets.");
                });

        var friendshipId = new FriendshipId(requesterPetId, addresseePetId);
        var friendship = new Friendship(friendshipId, requesterPet, addresseePet, FriendshipStatus.PENDING);

        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptFriendRequest(UUID addresseePetId, UUID requesterPetId) {
        var currentUser = getCurrentUser();
        var addresseePet = findPetById(addresseePetId);

        if (!addresseePet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of the pet accepting the request.");
        }

        var friendship = findFriendship(requesterPetId, addresseePetId);

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("This friend request is not pending and cannot be accepted.");
        }

        if (!friendship.getAddresseePet().equals(addresseePet)) {
            throw new IllegalStateException("This pet is not the recipient of the friend request.");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Transactional(readOnly = true)
    public List<FriendshipRequestResponse> getPendingRequestsForPet(UUID petId) {
        var currentUser = getCurrentUser();
        var pet = findPetById(petId);

        if (!pet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of this pet.");
        }

        return friendshipRepository.findByAddresseePetAndStatus(pet, FriendshipStatus.PENDING).stream()
                .map(friendship -> new FriendshipRequestResponse(
                        friendship.getRequesterPet().getId(),
                        friendship.getRequesterPet().getName(),
                        friendship.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional
    public void removeFriendship(UUID petId1, UUID petId2) {
        validateOwnershipForAction(petId1, petId2);
        var friendship = findFriendship(petId1, petId2);
        friendshipRepository.delete(friendship);
    }

    @Transactional
    public void blockFriendship(UUID petId1, UUID petId2) {
        validateOwnershipForAction(petId1, petId2);
        var friendship = findFriendship(petId1, petId2);
        friendship.setStatus(FriendshipStatus.BLOCKED);
        friendshipRepository.save(friendship);
    }

    @Transactional(readOnly = true)
    public FriendshipStatusResponse getFriendshipStatus(UUID petId1, UUID petId2) {
        var status = friendshipRepository.findFriendshipBetweenPets(petId1, petId2)
                .map(Friendship::getStatus)
                .orElse(null); // Will be serialized as null, frontend can interpret as "NONE"
        return new FriendshipStatusResponse(status);
    }

    private void validateOwnershipForAction(UUID petId1, UUID petId2) {
        var currentUser = getCurrentUser();
        var pet1 = findPetById(petId1);
        var pet2 = findPetById(petId2);

        if (!pet1.getOwner().equals(currentUser) && !pet2.getOwner().equals(currentUser)) {
            throw new SecurityException("You must be the owner of at least one of the pets to modify the friendship.");
        }
    }

    private Friendship findFriendship(UUID petId1, UUID petId2) {
        return friendshipRepository.findFriendshipBetweenPets(petId1, petId2)
                .orElseThrow(() -> new IllegalArgumentException("Friendship not found between the pets."));
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId).orElseThrow(() -> new IllegalStateException("User not found"));
    }

    private Pet findPetById(UUID petId) {
        return petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet with ID " + petId + " not found."));
    }
}
