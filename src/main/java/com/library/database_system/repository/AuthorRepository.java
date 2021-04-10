package com.library.database_system.repository;

import com.library.database_system.projections.IdFNameMNameLName;
import com.library.database_system.domain.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {

    @Query(value = "SELECT * FROM author a WHERE a.f_name = ?1 AND a.m_name = ?2 AND a.l_name = ?3", nativeQuery = true)
    Optional<Author> findAuthorByFullName(String fName, String mName, String lName);

    @Query(value = "SELECT * FROM author a WHERE a.f_name = ?1 AND a.m_name = ?2 AND a.l_name = ?3", nativeQuery = true)
    Optional<IdFNameMNameLName> findAuthorDTOByFullName(String fName, String mName, String lName);

    @Query(value = "SELECT * from author a WHERE a.f_name is NULL AND a.m_name IS NULL AND a.l_name IS NULL", nativeQuery = true)
    Optional<Author> findAuthorEmpty();

    @Query(value = "SELECT * from author", nativeQuery = true)
    Collection<IdFNameMNameLName> findAllAuthors();

    @Query(value = "SELECT * FROM author WHERE author.id = ?1", nativeQuery = true)
    Optional<IdFNameMNameLName> findByIdFullNameOnly(Long authorId);
}