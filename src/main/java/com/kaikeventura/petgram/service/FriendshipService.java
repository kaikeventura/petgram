package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.FriendshipId;
import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import com.kaikeventura.petgram.dto.FriendshipRequestResponse;
import com.kaikeventura.petgram.dto.RelationshipStatus;
import com.kaikeventura.petgram.dto.RelationshipStatusResponse;
import com.kaikeventura.petgram.event.FriendshipAcceptedEvent;
import com.kaikeventura.petgram.event.FriendshipRequestedEvent;
import com.kaikeventura.petgram.repository.FriendshipRepository;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final ApplicationEventPublisher eventPublisher;

    @Transactional
    public void sendFollowRequest(UUID requesterPetId, UUID targetPetId) {
        var currentUser = getCurrentUser();
        var requesterPet = findPetById(requesterPetId);

        if (!requesterPet.getOwner().equals(currentUser)) {
            throw new SecurityException("You can only send follow requests for your own pet.");
        }

        if (requesterPetId.equals(targetPetId)) {
            throw new IllegalArgumentException("A pet cannot follow itself.");
        }

        var targetPet = findPetById(targetPetId);

        friendshipRepository.findById(new FriendshipId(requesterPetId, targetPetId))
                .ifPresent(friendship -> {
                    throw new IllegalStateException("A follow request has already been sent or established.");
                });

        var friendshipId = new FriendshipId(requesterPetId, targetPetId);
        var friendship = new Friendship(friendshipId, requesterPet, targetPet, FriendshipStatus.PENDING);

        var savedFriendship = friendshipRepository.save(friendship);
        eventPublisher.publishEvent(new FriendshipRequestedEvent(this, savedFriendship));
    }

    @Transactional
    public void acceptFollowRequest(UUID targetPetId, UUID requesterPetId) {
        var currentUser = getCurrentUser();
        var targetPet = findPetById(targetPetId);

        if (!targetPet.getOwner().equals(currentUser)) {
            throw new SecurityException("You are not the owner of the pet accepting the request.");
        }

        var friendship = friendshipRepository.findById(new FriendshipId(requesterPetId, targetPetId))
                .orElseThrow(() -> new IllegalArgumentException("Follow request not found."));

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("This follow request is not pending.");
        }

        if (!friendship.getAddresseePet().equals(targetPet)) {
            throw new IllegalStateException("This pet is not the recipient of the follow request.");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        var savedFriendship = friendshipRepository.save(friendship);
        eventPublisher.publishEvent(new FriendshipAcceptedEvent(this, savedFriendship));
    }

    @Transactional
    public void unfollow(UUID requesterPetId, UUID targetPetId) {
        var currentUser = getCurrentUser();
        var requesterPet = findPetById(requesterPetId);

        if (!requesterPet.getOwner().equals(currentUser)) {
            throw new SecurityException("You can only unfollow for your own pet.");
        }

        var friendshipId = new FriendshipId(requesterPetId, targetPetId);
        if (!friendshipRepository.existsById(friendshipId)) {
            throw new IllegalArgumentException("You are not following this pet.");
        }
        friendshipRepository.deleteById(friendshipId);
    }

    @Transactional(readOnly = true)
    public List<FriendshipRequestResponse> getPendingRequests(UUID petId) {
        var currentUser = getCurrentUser();
        var pet = findPetById(petId);

        if (!pet.getOwner().equals(currentUser)) {
            throw new SecurityException("You can only view pending requests for your own pet.");
        }

        return friendshipRepository.findByAddresseePetAndStatus(pet, FriendshipStatus.PENDING).stream()
                .map(friendship -> new FriendshipRequestResponse(
                        friendship.getRequesterPet().getId(),
                        friendship.getRequesterPet().getName(),
                        friendship.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RelationshipStatusResponse getRelationshipStatus(UUID currentPetId, UUID targetPetId) {
        var currentUser = getCurrentUser();
        var currentPet = findPetById(currentPetId);

        if (!currentPet.getOwner().equals(currentUser)) {
            throw new SecurityException("You can only check the status for your own pet.");
        }

        Optional<Friendship> iFollowTarget = friendshipRepository.findById(new FriendshipId(currentPetId, targetPetId));
        Optional<Friendship> targetFollowsMe = friendshipRepository.findById(new FriendshipId(targetPetId, currentPetId));

        boolean iFollow = iFollowTarget.isPresent() && iFollowTarget.get().getStatus() == FriendshipStatus.ACCEPTED;
        boolean targetFollows = targetFollowsMe.isPresent() && targetFollowsMe.get().getStatus() == FriendshipStatus.ACCEPTED;
        boolean pendingSent = iFollowTarget.isPresent() && iFollowTarget.get().getStatus() == FriendshipStatus.PENDING;
        boolean pendingReceived = targetFollowsMe.isPresent() && targetFollowsMe.get().getStatus() == FriendshipStatus.PENDING;

        RelationshipStatus status;
        if (iFollow && targetFollows) {
            status = RelationshipStatus.MUTUAL;
        } else if (iFollow) {
            status = RelationshipStatus.FOLLOWING;
        } else if (targetFollows) {
            status = RelationshipStatus.FOLLOWED_BY;
        } else if (pendingSent) {
            status = RelationshipStatus.PENDING_SENT;
        } else if (pendingReceived) {
            status = RelationshipStatus.PENDING_RECEIVED;
        } else {
            status = RelationshipStatus.NONE;
        }

        return new RelationshipStatusResponse(status);
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
