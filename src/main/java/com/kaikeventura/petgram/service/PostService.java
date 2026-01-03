package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.Post;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.PostResponse;
import com.kaikeventura.petgram.repository.PetRepository;
import com.kaikeventura.petgram.repository.PostRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.PostMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PetRepository petRepository;
    private final S3StorageService storageService;
    private final PostMapper postMapper;

    @Transactional
    public PostResponse createPost(String caption, List<UUID> taggedPetIds, MultipartFile file) {
        var user = getCurrentUser();
        var photoUrl = storageService.uploadFile(file);

        var taggedPets = new HashSet<Pet>();
        if (taggedPetIds != null && !taggedPetIds.isEmpty()) {
            taggedPets.addAll(petRepository.findAllById(taggedPetIds));
            // Basic validation: ensure all provided IDs were found.
            if (taggedPets.size() != taggedPetIds.size()) {
                throw new IllegalArgumentException("One or more tagged pets were not found.");
            }
        }

        var post = new Post(
                null,
                photoUrl,
                caption,
                user,
                taggedPets,
                Collections.emptySet(),
                Collections.emptyList(),
                null,
                null
        );

        var savedPost = postRepository.save(post);
        return postMapper.toPostResponse(savedPost);
    }

    @Transactional(readOnly = true)
    public Page<PostResponse> getNewsFeed(Pageable pageable) {
        var user = getCurrentUser();
        var pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        var posts = postRepository.findNewsFeedForUser(user.getId(), pageRequest);
        return posts.map(postMapper::toPostResponse);
    }

    @Transactional(readOnly = true)
    public PostResponse findPostById(UUID postId) {
        return postRepository.findById(postId)
                .map(postMapper::toPostResponse)
                .orElseThrow(() -> new IllegalArgumentException("Post not found."));
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
