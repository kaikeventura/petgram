package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Post;
import com.kaikeventura.petgram.dto.PostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {UserMapper.class, CommentMapper.class})
public interface PostMapper {

    @Mapping(target = "likeCount", expression = "java(post.getLikes() != null ? post.getLikes().size() : 0)")
    @Mapping(target = "commentCount", expression = "java(post.getComments() != null ? post.getComments().size() : 0)")
    @Mapping(source = "author", target = "author")
    PostResponse toPostResponse(Post post);
}
