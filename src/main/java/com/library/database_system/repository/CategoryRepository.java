package com.library.database_system.repository;

import com.library.database_system.projections.CategoryIdName;
import com.library.database_system.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Optional<Category> findByName(String name);

    @Query(value = "SELECT * FROM category", nativeQuery = true)
    Collection<CategoryIdName> getIdName();
}
