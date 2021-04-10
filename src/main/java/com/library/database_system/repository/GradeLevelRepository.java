package com.library.database_system.repository;

import com.library.database_system.domain.GradeLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GradeLevelRepository extends JpaRepository<GradeLevel, Long> {

    Optional<GradeLevel> findByLevel(int level);
}
