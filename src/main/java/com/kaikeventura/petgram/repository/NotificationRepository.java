package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Notification;
import com.kaikeventura.petgram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    List<Notification> findByRecipientAndIsReadOrderByCreatedAtDesc(User recipient, boolean isRead);

    long countByRecipientAndIsRead(User recipient, boolean isRead);

    List<Notification> findBySubjectPetIdAndIsReadOrderByCreatedAtDesc(UUID subjectPetId, boolean isRead);

    long countBySubjectPetIdAndIsRead(UUID subjectPetId, boolean isRead);

    @Modifying
    @Query("UPDATE Notification n SET n.isRead = true WHERE n.subjectPet.id = :subjectPetId AND n.isRead = false")
    void markAllAsReadForSubjectPet(@Param("subjectPetId") UUID subjectPetId);
}
