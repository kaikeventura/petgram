package com.kaikeventura.petgram.dto;

public enum RelationshipStatus {
    /**
     * No relationship exists between the two pets.
     * UI Action: Show "Follow" button.
     */
    NONE,

    /**
     * The current pet has sent a follow request, but it has not been accepted yet.
     * UI Action: Show "Requested" button.
     */
    PENDING_SENT,

    /**
     * The target pet has sent a follow request to the current pet.
     * UI Action: Show "Confirm" or "Accept" button.
     */
    PENDING_RECEIVED,

    /**
     * The current pet follows the target pet, but the target does not follow back.
     * UI Action: Show "Following" button.
     */
    FOLLOWING,

    /**
     * The target pet follows the current pet, but the current pet does not follow back.
     * UI Action: Show "Follow Back" button.
     */
    FOLLOWED_BY,

    /**
     * Both pets follow each other.
     * UI Action: Show "Following" or "Friends" button.
     */
    MUTUAL,

    /**
     * The current pet has requested to follow the target pet back.
     * UI Action: Show "Requested" button.
     */
    PENDING_FOLLOW_BACK,

    /**
     * The current pet has a pending follow-back request from the target pet.
     * UI Action: Show "Accept Follow Back" button.
     */
    ACCEPT_FOLLOW_BACK
}
