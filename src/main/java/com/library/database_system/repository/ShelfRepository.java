package com.library.database_system.repository;

import com.library.database_system.domain.Shelf;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ShelfRepository extends JpaRepository<Shelf, Long> {
    Optional<Shelf> findByName(String name);
}
