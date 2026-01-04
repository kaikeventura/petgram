package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Comment;
import com.kaikeventura.petgram.dto.CommentResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {
    @Mapping(source = "author", target = "author")
    CommentResponse toCommentResponse(Comment comment);
}
