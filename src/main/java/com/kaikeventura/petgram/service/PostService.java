package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Post;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.dto.PostResponse;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final S3StorageService storageService;
    private final PostMapper postMapper;

    @Transactional
    public PostResponse createPost(String caption, MultipartFile file) {
        var user = getCurrentUser();
        var photoUrl = storageService.uploadFile(file);

        var post = new Post(
                null,
                photoUrl,
                caption,
                user,
                Collections.emptySet(),
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
        // Enforce business rule: Ignore client-side sorting and use only pagination info.
        // The sorting is fixed in the repository query.
        var pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());
        var posts = postRepository.findNewsFeedForUser(user.getId(), pageRequest);
        return posts.map(postMapper::toPostResponse);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
