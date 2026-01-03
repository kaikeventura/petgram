package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import com.kaikeventura.petgram.repository.FriendshipRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FriendshipService {

    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    @Transactional
    public void sendFriendRequest(UUID addresseeId) {
        var requester = getCurrentUser();
        var addressee = userRepository.findById(addresseeId)
                .orElseThrow(() -> new IllegalArgumentException("Addressee not found"));

        // Create a new friendship request
    }

    @Transactional
    public void acceptFriendRequest(UUID requesterId) {
        var addressee = getCurrentUser();
        var requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new IllegalArgumentException("Requester not found"));

        // Find the pending request and update its status to ACCEPTED
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
