package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Post;
import com.kaikeventura.petgram.dto.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface PostMapper {

    @Mapping(target = "likeCount", expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)")
    @Mapping(source = "author", target = "author")
    @Mapping(source = "comments", target = "comments")
    PostResponse toPostResponse(Post post);
}
