package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Friendship;
import com.kaikeventura.petgram.domain.FriendshipId;
import com.kaikeventura.petgram.domain.User;
import com.kaikeventura.petgram.domain.enums.FriendshipStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FriendshipRepository extends JpaRepository<Friendship, FriendshipId> {

    @Query("SELECT f.addressee FROM Friendship f WHERE f.requester = :user AND f.status = :status")
    List<User> findFriendsByRequesterAndStatus(@Param("user") User user, @Param("status") FriendshipStatus status);

    @Query("SELECT f.requester FROM Friendship f WHERE f.addressee = :user AND f.status = :status")
    List<User> findFriendsByAddresseeAndStatus(@Param("user") User user, @Param("status") FriendshipStatus status);
}
