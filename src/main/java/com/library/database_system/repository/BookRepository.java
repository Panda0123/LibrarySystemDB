package com.library.database_system.repository;

import com.library.database_system.domain.Book;
import com.library.database_system.projections.CollectionProj;
import com.library.database_system.resulttransformer.BookTransformer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long>, CustomBookRepository{

    @Query(value = "SELECT bk FROM Book bk WHERE bk.ISBN IS NOT NULL AND bk.ISBN = ?1")
    Optional<Book> findByISBN(String ISBN);

    List<BookTransformer> getBooksDetails();

    @Query(value = "SELECT book FROM Book book")
    Collection<CollectionProj> getBooksCollection();
}
