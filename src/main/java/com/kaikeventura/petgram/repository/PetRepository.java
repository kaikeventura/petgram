package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findByOwner(User owner);

    List<Pet> findByOwnerId(UUID ownerId);

    @Query("SELECT p FROM Pet p WHERE p.name ILIKE %:name%")
    List<Pet> findByNameContainingIgnoreCase(@Param("name") String name, Pageable pageable);
}
