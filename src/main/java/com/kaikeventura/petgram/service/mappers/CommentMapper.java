package com.kaikeventura.petgram.service.mappers;

import com.kaikeventura.petgram.domain.Comment;
import com.kaikeventura.petgram.dto.CommentResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = UserMapper.class)
public interface CommentMapper {
    CommentResponse toCommentResponse(Comment comment);
}
