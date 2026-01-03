package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.FriendshipId;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import com.kaikeventura.petgram.dto.FriendshipRequestResponse;
import com.kaikeventura.petgram.repository.FriendshipRepository;
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

    @Transactional
    public void sendFriendRequest(UUID addresseeId) {
        var requester = getCurrentUser();
        if (requester.getId().equals(addresseeId)) {
            throw new IllegalArgumentException("You cannot send a friend request to yourself.");
        }

        var addressee = findUserById(addresseeId);

        friendshipRepository.findFriendshipBetweenUsers(requester.getId(), addresseeId)
                .ifPresent(friendship -> {
                    throw new IllegalStateException("A friendship request already exists or has been established.");
                });

        var friendshipId = new FriendshipId(requester.getId(), addresseeId);
        var friendship = new Friendship(friendshipId, requester, addressee, FriendshipStatus.PENDING);

        friendshipRepository.save(friendship);
    }

    @Transactional
    public void acceptFriendRequest(UUID requesterId) {
        var addressee = getCurrentUser(); // The user accepting the request
        var requester = findUserById(requesterId);

        var friendship = friendshipRepository.findFriendshipBetweenUsers(requesterId, addressee.getId())
                .orElseThrow(() -> new IllegalArgumentException("Friendship request not found."));

        // Ensure the current user is the one who received the request
        if (!friendship.getAddressee().equals(addressee)) {
            throw new IllegalStateException("You are not authorized to accept this friend request.");
        }

        if (friendship.getStatus() != FriendshipStatus.PENDING) {
            throw new IllegalStateException("This friend request is not pending and cannot be accepted.");
        }

        friendship.setStatus(FriendshipStatus.ACCEPTED);
        friendshipRepository.save(friendship);
    }

    @Transactional(readOnly = true)
    public List<FriendshipRequestResponse> getPendingRequests() {
        var currentUser = getCurrentUser();
        var pendingFriendships = friendshipRepository.findByAddresseeAndStatus(currentUser, FriendshipStatus.PENDING);

        return pendingFriendships.stream()
                .map(friendship -> new FriendshipRequestResponse(
                        friendship.getRequester().getId(),
                        friendship.getRequester().getName(),
                        friendship.getCreatedAt()
                ))
                .collect(Collectors.toList());
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return findUserById(userId);
    }

    private User findUserById(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found."));
    }
}
