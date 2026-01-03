package com.kaikeventura.petgram.event;

import com.kaikeventura.petgram.domain.Like;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class PostLikedEvent extends ApplicationEvent {
    private final Like like;

    public PostLikedEvent(Object source, Like like) {
        super(source);
        this.like = like;
    }
}
