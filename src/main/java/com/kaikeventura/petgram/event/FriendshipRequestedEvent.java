package com.kaikeventura.petgram.event;

import com.kaikeventura.petgram.domain.Friendship;
import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FriendshipRequestedEvent extends ApplicationEvent {
    private final Friendship friendship;

    public FriendshipRequestedEvent(Object source, Friendship friendship) {
        super(source);
        this.friendship = friendship;
    }
}
