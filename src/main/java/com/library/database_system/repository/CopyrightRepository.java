package com.library.database_system.repository;

import com.library.database_system.domain.Copyright;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CopyrightRepository extends JpaRepository<Copyright, Long> {

    Optional<Copyright> findByName(String name);

    Optional<Copyright> findByNameAndYear(String name, Short year);
}
