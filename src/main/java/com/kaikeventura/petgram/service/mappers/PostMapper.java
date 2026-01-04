package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Post;
import com.kaikeventura.petgram.dto.PostResponse;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class, S3UrlMapper.class})
public abstract class PostMapper {

    @Mapping(target = "likeCount", expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)")
    @Mapping(target = "commentCount", expression = "java(post.getComments() != null ? post.getComments().size() : 0)")
    @Mapping(target = "isLiked", expression = "java(post.getLikes() != null && viewerId != null && post.getLikes().stream().anyMatch(like -> like.getPet().getId().equals(viewerId)))")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "photoUrl", target = "photoUrl", qualifiedByName = "generatePresignedUrl")
    public abstract PostResponse toPostResponse(Post post, @Context UUID viewerId);
}
