package com.kaikeventura.petgram.event;

import com.kaikeventura.petgram.domain.Comment;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostCommentedEvent extends ApplicationEvent {
    private final Comment comment;

    public PostCommentedEvent(Object source, Comment comment) {
        super(source);
        this.comment = comment;
    }
}
