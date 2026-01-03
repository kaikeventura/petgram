package com.kaikeventura.petgram.repository;

import com.kaikeventura.petgram.domain.Pet;
import com.kaikeventura.petgram.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface PetRepository extends JpaRepository<Pet, UUID> {
    List<Pet> findByOwner(User owner);
}
