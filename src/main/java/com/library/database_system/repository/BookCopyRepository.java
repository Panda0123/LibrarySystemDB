package com.library.database_system.repository;

import com.library.database_system.domain.BookCopy;
import com.library.database_system.projections.BookCopyProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    @Query("SELECT bookCopy FROM BookCopy bookCopy WHERE bookCopy.originalBook.id = ?1")
    Collection<BookCopyProj> getCopyProj(Long bookId);
}
