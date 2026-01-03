package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.dto.UserProfileResponse;
import com.kaikeventura.petgram.dto.UserUpdateRequest;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.PostMapper;
import com.kaikeventura.petgram.service.mappers.UserProfileMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserProfileMapper userProfileMapper;
    private final PostMapper postMapper;

    @Transactional(readOnly = true)
    public UserProfileResponse getCurrentUserProfile() {
        var user = getCurrentUser();
        return userProfileMapper.toUserProfileResponse(user);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getCurrentUserPosts(Pageable pageable) {
        var user = getCurrentUser();
        return postRepository.findByAuthorOrderByCreatedAtDesc(user, pageable)
                .map(postMapper::toPostResponse);
    }

    @Transactional
    public UserProfileResponse updateCurrentUser(UserUpdateRequest userUpdateRequest) {
        var user = getCurrentUser();
        user.setName(userUpdateRequest.name());
        var updatedUser = userRepository.save(user);
        return userProfileMapper.toUserProfileResponse(updatedUser);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
