package com.library.database_system.repository;

import com.library.database_system.domain.BookCopy;
import com.library.database_system.projections.BookCopyProj;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {
    @Query("SELECT bkCpy.id AS id, bkCpy.copy_num AS copy_num, bkCpy.status AS status FROM com.library.database_system.domain.BookCopy bkCpy WHERE bkCpy.originalBook.id = ?1")
    List<BookCopyProj> getCopyProj(Long bookId);
}
