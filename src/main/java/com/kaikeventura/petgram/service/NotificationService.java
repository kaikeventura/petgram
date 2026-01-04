package com.kaikeventura.petgram.service;

import com.kaikeventura.petgram.domain.Notification;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.NotificationType;
import com.kaikeventura.petgram.dto.NotificationResponse;
import com.kaikeventura.petgram.event.FriendshipAcceptedEvent;
import com.kaikeventura.petgram.event.FriendshipRequestedEvent;
import com.kaikeventura.petgram.event.PostCommentedEvent;
import com.kaikeventura.petgram.event.PostLikedEvent;
import com.kaikeventura.petgram.repository.NotificationRepository;
import com.kaikeventura.petgram.repository.UserRepository;
import com.kaikeventura.petgram.service.mappers.NotificationMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;
    private final NotificationMapper notificationMapper;

    // --- Event Listeners ---

    @Async
    @EventListener
    public void handlePostLikedEvent(PostLikedEvent event) {
        var like = event.getLike();
        var recipient = like.getPost().getAuthor().getOwner();
        var actor = like.getPet();

        var message = String.format("%s liked your pet's post.", actor.getName());
        var link = String.format("/posts/%s", like.getPost().getId());

        createNotification(recipient, NotificationType.POST_LIKE, message, link);
    }

    @Async
    @EventListener
    public void handlePostCommentedEvent(PostCommentedEvent event) {
        var comment = event.getComment();
        var recipient = comment.getPost().getAuthor().getOwner();
        var actor = comment.getAuthor();

        var message = String.format("%s commented on your pet's post.", actor.getName());
        var link = String.format("/posts/%s", comment.getPost().getId());

        createNotification(recipient, NotificationType.POST_COMMENT, message, link);
    }

    @Async
    @EventListener
    public void handleFriendshipRequestedEvent(FriendshipRequestedEvent event) {
        var friendship = event.getFriendship();
        var recipient = friendship.getAddresseePet().getOwner();
        var actorPet = friendship.getRequesterPet();

        var message = String.format("%s sent a friend request to your pet %s.", actorPet.getName(), friendship.getAddresseePet().getName());
        var link = String.format("/pets/%s", actorPet.getId());

        createNotification(recipient, NotificationType.FRIENDSHIP_REQUEST, message, link);
    }

    @Async
    @EventListener
    public void handleFriendshipAcceptedEvent(FriendshipAcceptedEvent event) {
        var friendship = event.getFriendship();
        var recipient = friendship.getRequesterPet().getOwner();
        var actorPet = friendship.getAddresseePet();

        var message = String.format("%s is now friends with your pet %s.", actorPet.getName(), friendship.getRequesterPet().getName());
        var link = String.format("/pets/%s", actorPet.getId());

        createNotification(recipient, NotificationType.FRIENDSHIP_ACCEPTED, message, link);
    }

    private void createNotification(User recipient, NotificationType type, String message, String link) {
        var notification = new Notification(null, recipient, type, message, link, false, null);
        notificationRepository.save(notification);
    }

    // --- API Methods ---

    @Transactional(readOnly = true)
    public List<NotificationResponse> getUnreadNotifications() {
        var user = getCurrentUser();
        return notificationRepository.findByRecipientAndIsReadOrderByCreatedAtDesc(user, false).stream()
                .map(notificationMapper::toNotificationResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAllAsRead() {
        var user = getCurrentUser();
        notificationRepository.markAllAsReadForRecipient(user);
    }

    private User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userId = UUID.fromString(principal.getUsername());
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalStateException("User not found"));
    }
}
